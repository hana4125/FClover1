package hello.fclover.initializer;

import hello.fclover.mybatis.mapper.GoodsMapper;
import hello.fclover.mybatis.mapper.SellerMapper;
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
    private final SellerMapper sellerMapper;
    private final BatchDataInserter batchDataInserter;


    @Bean
    public ApplicationRunner initDatabaseRunner() {
        return args -> {

//            int sellerCount = sellerMapper.countAllSeller();
//            log.info("seller 테이블 데이터 개수 = {}", sellerCount);
//            if (sellerCount > 0) {
//                log.info("판매자 정보가 이미 초기화 되었습니다. 더미 데이터 삽입을 스킵합니다.");
//            } else {
//                log.info("판매자 정보가 비어 있습니다. 더미 데이터 초기화를 시작합니다.");
//
//                int totalCount = 1000;
//
//                int chunkSize = 1000;
//
//                int threadCount = 4;
//
//                batchDataInserter.insertLargeSellerDataParallel(totalCount, chunkSize, threadCount);
//            }

            long goodsCount = goodsMapper.countAll();
            log.info("goods 테이블 데이터 개수 = {}", goodsCount);
            if (goodsCount > 0) {
                log.info("상품 정보가 이미 초기화 되었습니다. 더미 데이터 삽입을 스킵합니다.");
            } else {
                log.info("상품 정보가 비어 있습니다. 더미 데이터 초기화를 시작합니다.");

                //dropFullTextIndexSafe();

                // 전체 데이터 갯수
                int totalCount = 500_000;

                // rds 사양에 따라서 유동적으로 조절
                // 청크 사이즈 (500 ~ 2000)
                int chunkSize = 1000;
                // 스레드 갯수 (4 ~ 20)
                int threadCount = 4;
                batchDataInserter.insertLargeGoodsDataParallel(totalCount, chunkSize, threadCount);

                //createFullTextIndexSafe();
            }
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
