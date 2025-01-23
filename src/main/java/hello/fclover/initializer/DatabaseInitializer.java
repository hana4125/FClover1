package hello.fclover.initializer;

import hello.fclover.mybatis.mapper.GoodsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DatabaseInitializer {

    private final GoodsMapper goodsMapper;
    private final BatchDataInserter batchDataInserter;


    @Bean
    public ApplicationRunner initDatabaseRunner() {
        return args -> {
          long count = goodsMapper.countAll();
          log.info("GOODS 테이블 갯수 = {}", count);
          if (count > 0) {
              log.info("데이터베이스가 이미 초기화 되었습니다. 더미 데이터 삽입을 스킵합니다.");
              return;
          }

          log.info("데이터베이스가 비어 있습니다. 더미 데이터 초기화를 시작합니다.");

          //dropFullTextIndexSafe();

          // 전체 데이터 갯수
          int totalCount = 500_000;
          // 청크 사이즈
          int chunkSize = 5_000;
          // 스레드 갯수
          int threadCount = 10;
          batchDataInserter.insertLargeDataParallel(totalCount, chunkSize, threadCount);

          //createFullTextIndexSafe();
        };
    }

//    private void dropFullTextIndexSafe() {
//        try {
//            log.info("FullTextIndex인 ft_goods_idx 제거중...");
//            goodsMapper.dropFullTextIndex();
//        } catch (Exception e) {
//            log.warn("FullTextIndex의 삭제에 실패했습니다 (존재하지 않을 수 있음): {}", e.getMessage());
//        }
//    }
//
//    private void createFullTextIndexSafe() {
//        try {
//            log.info("FullTextIndex인 ft_goods_idx 생성중...");
//            goodsMapper.createFullTextIndex();
//        } catch (Exception e) {
//            log.info("FullTextIndex의 생성에 실패했습니다: {}", e.getMessage());
//        }
//    }
}
