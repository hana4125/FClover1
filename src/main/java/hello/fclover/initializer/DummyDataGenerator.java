package hello.fclover.initializer;

import hello.fclover.domain.Goods;
import hello.fclover.domain.Seller;
import hello.fclover.domain.Stock;
import hello.fclover.mybatis.mapper.CategoryMapper;
import hello.fclover.mybatis.mapper.NoticeMapper;
import jakarta.annotation.PostConstruct;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Profile;
import hello.fclover.domain.Notice;  // Notice 엔티티


@Slf4j
@Component
@RequiredArgsConstructor
public class DummyDataGenerator {

    private final Faker faker = new Faker(new Locale("ko"));
    private final CategoryMapper categoryMapper;
    private final NoticeMapper noticeRepository;

    private int maximumCateNo;

    @PostConstruct
    private void initMaxCateNo() {
        this.maximumCateNo = categoryMapper.countAll();
        log.info("초기화 된 maximumCateNo: {}", maximumCateNo);
    }

    // 전체 totalCount 개를, chunkSize 단위로 나누어 생성해주는 Iterator 를 반환
    public Iterator<List<Goods>> generateGoodsInChunks(int totalCount, int chunkSize) {
        return new Iterator<>() {
          private int generateSoFar = 0;

          @Override
          public boolean hasNext() {
              return generateSoFar < totalCount;
          }

          @Override
          public List<Goods> next() {
              // 이번에 생성할 개수 계산(마지막 chunk 에는 나머지)
              int remaining = totalCount - generateSoFar;
              int size = Math.min(chunkSize, remaining);

              List<Goods> chunk = new ArrayList<>(size);
              for (int i = 0; i < size; i++) {
                  chunk.add(generateOneGood());
              }
              generateSoFar += size;
              return chunk;
          }
        };
    }

    // 단일 goods 객체를 생성
    private Goods generateOneGood() {

        int SELLER_START_NUM = 1;
        int SELLER_COUNT = 500;
        Long sellerNo = (long) faker.number().numberBetween(SELLER_START_NUM, SELLER_COUNT + 1);

        int MINIMUM_CATE_NO = 1;
        // 초기화 시점에서 가져온 maximumCateNo를 재사용
        int cateNo = faker.number().numberBetween(MINIMUM_CATE_NO, maximumCateNo + 1);

        String goodsName = faker.book().title() + faker.number().numberBetween(1, 100);

        String goodsContent = "이 책은 " + faker.book().genre() + " 장르의 책으로 " + faker.lorem().sentence().toLowerCase() + " 이다.";

        int MINIMUM_PRICE = 5000;
        int MAXIMUM_PRICE = 100000;
        int minDiv = MINIMUM_PRICE / 1000;
        int maxDiv = MAXIMUM_PRICE / 1000;
        int goodsPrice = faker.number().numberBetween(minDiv, maxDiv + 1) * 1000;

        String goodsWriter = faker.book().author();

        String writerContent = "작가 " + goodsWriter + " 는 " + faker.lorem().sentence().toLowerCase() + " 이다.";

        Instant randomPastInstant = faker.timeAndDate().past(3650, TimeUnit.of(ChronoUnit.DAYS));
        LocalDate goodsCreateAt = LocalDate.ofInstant(randomPastInstant, ZoneId.systemDefault());

        int MINIMUM_PAGE_COUNT = 10;
        int MAXIMUM_PAGE_COUNT = 700;
        int goodsPageCount = faker.number().numberBetween(MINIMUM_PAGE_COUNT, MAXIMUM_PAGE_COUNT + 1);

        int minSize = 70;
        int maxSize = 300;
        int firstNumber = faker.number().numberBetween(minSize, maxSize);
        int secondNumber = faker.number().numberBetween(minSize, maxSize);
        String goodsBookSize = firstNumber + "*" + secondNumber;

        return Goods.builder()
                .sellerNo(sellerNo)
                .cateNo(cateNo)
                .goodsName(goodsName)
                .goodsContent(goodsContent)
                .goodsPrice(goodsPrice)
                .goodsWriter(goodsWriter)
                .writerContent(writerContent)
                .goodsCreateAt(goodsCreateAt)
                .goodsPageCount(goodsPageCount)
                .goodsBookSize(goodsBookSize)
                .build();
    }

    // 단일 Seller 생성
    private Seller generateOneSeller() {

        // TODO : Seller 더미 데이터 생성하기

        return Seller.builder()
                .build();
    }

    private Stock generateOneStock() {

        // TODO : Stock 더미 데이터 생성하기 -> Goods 객체와 연계해서 생성하기


        return Stock.builder().build();
    }

    @PostConstruct
    @Profile("dev")
    public void generateNoticeData() {
        // 기존 데이터 삭제
        noticeRepository.deleteAll();

        // 공지사항 제목 템플릿
        String[] noticeTemplates = {
                "[공지사항] 시스템 점검 안내",
                "[공지사항] 개인정보 처리방침 변경 안내",
                "[공지사항] 시스템 업데이트 안내",
                "[공지사항] 이용약관 개정 안내",
                "[공지사항] 택배 배송지연 지역 안내",
                "[공지사항] 마일리지 통합 포인트 전환 안내",
                "[공지사항] 마케팅 정보 수신동의 확인 안내",
                "[공지사항] 네잎클로버 리워드 혜택 변경 안내",
                "[공지사항] **카드 서비스 종료 안내",
                "[공지사항] 상품권 서비스점검에 따른 사용제한 안내"
        };

        // 이벤트 제목 템플릿
        String[] eventTemplates = {
                "[이벤트] 신규 회원 특별 이벤트",
                "[이벤트] *월 출석 이벤트",
                "[이벤트] 네잎클로버 적립금 혜택",
                "[이벤트] 2만원 이상 무료배송 이벤트",
                "[이벤트] 첫결제 혜택 이벤트",
                "[이벤트] 리뷰 이벤트",
                "[이벤트] 추천도서 이벤트",
                "[이벤트] 작가와의 만남 이벤트",
                "[이벤트] 수험서/참고서 브랜드전",
                "[이벤트] 나만의 베스트셀러"
        };

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate now = LocalDate.now();

        for (int i = 0; i < 500; i++) {
            Notice notice = new Notice();

            // 공지사항과 이벤트 중 랜덤 선택
            String title;
            if (faker.random().nextBoolean()) {
                title = noticeTemplates[faker.random().nextInt(noticeTemplates.length)];
            } else {
                title = eventTemplates[faker.random().nextInt(eventTemplates.length)];
            }

            long randomDays = faker.number().numberBetween(0, 365);
            LocalDate randomDate = now.minusDays(randomDays);
            String formattedDate = randomDate.format(formatter);

            notice.setNotititle(title);
            notice.setNotiname("admin");
            notice.setNotidate(formattedDate);

            try {
                noticeRepository.save(notice);
                log.info("Dummy notice created: {}", notice);
            } catch (Exception e) {
                log.error("Failed to create dummy notice", e);
            }
        }
    }
}
