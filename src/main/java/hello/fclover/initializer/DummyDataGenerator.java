package hello.fclover.initializer;

import hello.fclover.domain.Goods;
import hello.fclover.mybatis.mapper.CategoryMapper;
import jakarta.annotation.PostConstruct;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DummyDataGenerator {

    private final Faker faker = new Faker(new Locale("ko"));
    private final CategoryMapper categoryMapper;

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
        Integer cateNo = faker.number().numberBetween(MINIMUM_CATE_NO, maximumCateNo + 1);

        String goodsName = faker.book().title();

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

        int MINIMUM_GOODS_COUNT = 0;
        int MAXIMUM_GOODS_COUNT = 10000;
        int goodsCount = faker.number().numberBetween(MINIMUM_GOODS_COUNT, MAXIMUM_GOODS_COUNT);

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
//                .goodsCount(goodsCount)
                .goodsPageCount(goodsPageCount)
                .goodsBookSize(goodsBookSize)
                .build();
    }
}
