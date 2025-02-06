package hello.fclover.task;

import hello.fclover.dto.SearchLogDTO;
import hello.fclover.mybatis.mapper.SearchLogMapper;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PopularKeywordBatchTask {

    private static final double DECAY_COEFFICIENT = 0.3;

    private final SearchLogMapper searchLogMapper;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 매 정시(분 :0)에 실행되도록 스케줄링
     * cron 표현식: "0 0 * * * *" -> 매 정시 0분 0초
     */
    @Scheduled(cron="0 0 * * * *")
    public void processAndUpdate(){

        long batchStartTime = System.currentTimeMillis();
        log.info("배치 작업 시작");

        LocalDateTime now = LocalDateTime.now();

        // 최근 24 시간의 로그 조회
        LocalDateTime startTime = now.minusHours(24);
        log.info("배치 시작: {} ~ {} 까지의 로그 조회", now, startTime);

        List<SearchLogDTO> logs = searchLogMapper.findLogsBetween(startTime, now);
        log.info("조회된 로그 개수: {}", logs.size());

        // 조건별 집계 결과를 저장
        // key : Redis key (예: "popular:남성:20"), value: (keyword -> 누적 가중치)
        Map<String, Map<String, Double>> aggregates = new HashMap<>();

        for (SearchLogDTO log : logs) {
            String keyword = log.getSearchKeyword();

            // 성별이 "M", "F"가 아닐 경우 "ALL" 처리
            String gender = ("M".equals(log.getMemberGender()) || "F".equals(log.getMemberGender())) ? log.getMemberGender() : "ALL";
            String ageCategory = log.getMemberAgeRange() == null ? "ALL" : log.getMemberAgeRange();

            // 경과 시간(시간 단위)
            double elapsedHours = Duration.between(log.getSearchDatetime(), now).toMinutes() / 60.0;
            // 24시간 이내의 로그만 처리 (그 외는 무시)
            if ( elapsedHours >= 24 ) {
                continue;
            }

            // 시간대별 가중치 부여 (지수 감쇠 모델 적용)
            double weight = calculateWeight(elapsedHours);

            // 조건별 집계: 전체(ALL/ALL), 성별(예: 남성/ALL), 연령(ALL/ageCategory), 복합(성별/ageCategory)
            String[][] conditions = {
                    {"ALL", "ALL"},
                    {gender, "ALL"},
                    {"ALL", ageCategory},
                    {gender, ageCategory}
            };

            for (String[] cond : conditions) {
                String redisKey = "popular:" + cond[0] + ":" + cond[1];
                aggregates.putIfAbsent(redisKey, new HashMap<>());
                Map<String, Double> keywordScoreMap = aggregates.get(redisKey);
                keywordScoreMap.put(keyword, keywordScoreMap.getOrDefault(keyword, 0.0) + weight);
            }
        }

        // 집계 결과를 Redis 에 업데이트 (기존 데이터 삭제 후 새롭게 등록)
        aggregates.forEach((redisKey, keywordScoreMap) -> {
            redisTemplate.delete(redisKey);
            keywordScoreMap.forEach((keyword, score) -> redisTemplate.opsForZSet().add(redisKey, keyword, score));
            log.info("Redis key '{}' 업데이트 완료. 항목 수: {}", redisKey, keywordScoreMap.size());
        });

        long batchEndTime = System.currentTimeMillis();
        long batchTime = batchEndTime - batchStartTime;
        log.info("배치 작업 종료, 소요 시간 : {} ms", batchTime);
    }

    /**
     * 경과 시간에 따라 가중치를 계산한다.
     * - 0 ≤ elapsed < 3: 1시간 단위로 exp(-0.3 * floor(elapsed))
     * - 3 ≤ elapsed < 6: 구간 [3,6)의 1시간 단위 가중치 평균
     * - 6 ≤ elapsed < 12: 구간 [6,12)의 1시간 단위 가중치 평균
     * - 12 ≤ elapsed < 24: 구간 [12,24)의 1시간 단위 가중치 평균
     */
    private double calculateWeight(double elapsedHours) {
        if (elapsedHours < 3) {
            // 0~3시간: 1시간 단위 (floor 처리)
            int bucketHour = (int) Math.floor(elapsedHours);
            return Math.exp(-DECAY_COEFFICIENT * bucketHour);
        } else if (elapsedHours < 6) {
            // 3~6시간: 평균(3시간 단위)
            return averageDecay(3, 6);
        } else if (elapsedHours < 12) {
            // 6~12시간: 평균(6시간 단위)
            return averageDecay(6, 12);
        } else { // elapsedHours < 24
            // 12~24시간: 평균(12시간 단위)
            return averageDecay(12, 24);
        }
    }

    /**
     * 지정된 시작시간(t1)과 종료시간(t2) 사이(정수시간 단위)의 지수 감쇠값의 평균을 계산
     * 예) t1=3, t2=6이면, 시간값 3, 4, 5의 exp(-0.3*t)를 평균내어 반환
     */
    private double averageDecay(int t1, int t2) {
        double sum = 0;
        int count = t2 - t1;  // 예: 3~6는 3시간 간격
        for (int t = t1; t < t2; t++) {
            sum += Math.exp(-DECAY_COEFFICIENT * t);
        }
        return sum / count;
    }

}
