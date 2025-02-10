package hello.fclover.initializer;

import hello.fclover.domain.Goods;
import hello.fclover.domain.Seller;
import hello.fclover.mybatis.mapper.GoodsMapper;
import hello.fclover.mybatis.mapper.SellerMapper;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BatchDataInserter {

    private final DummyDataGenerator dummyDataGenerator;
    private final SqlSessionTemplate batchSqlSessionTemplate;

    /**
     * 병렬 + Chunk 단위로 대량 데이터를 Insert
     * @param totalCount 총 몇 개 생성할지
     * @param chunkSize 한 번에 몇 건씩 chunk를 생성하고 삽입할지
     * @param threadCount 병렬 스레드 개수
     */
    public void insertLargeGoodsDataParallel(int totalCount, int chunkSize, int threadCount) {
        log.info("대용량 데이터 병렬 삽입 시작 - totalCount={}, chunkSize={}, threadCount={}", totalCount, chunkSize, threadCount);
        long startTime = System.currentTimeMillis();
        // Iterator 로 Chunk 단위 Book 생성
        Iterator<List<Goods>> chunkIterator = dummyDataGenerator.generateGoodsInChunks(totalCount, chunkSize);

        // 병렬 처리를 위한 ExecutorService
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        // 각 Chunk 마다 Submit
        int chunkIndex = 0;
        List<Future<?>> futures = new CopyOnWriteArrayList<>();

        while (chunkIterator.hasNext()) {
            final int currentChunkIndex = chunkIndex;
            final List<Goods> chunkData = chunkIterator.next();

            // 스레드 풀에 작업을 제출
            Future<?> future = executor.submit(() -> {
                // 스레드 내에서 독립적인 BATCH 세션을 열어서 Insert
                SqlSessionFactory sessionFactory = batchSqlSessionTemplate.getSqlSessionFactory();

                try (SqlSession session = sessionFactory.openSession(ExecutorType.BATCH)) {
                    GoodsMapper mapper = session.getMapper(GoodsMapper.class);
                    mapper.insertGoods(chunkData);

                    // flush + commit
                    session.flushStatements();
                    session.commit();
                } catch (Exception e) {
                    log.error("다음의 chunkIndex에서 오류 발생 chunkIndex={}", currentChunkIndex, e);
                }

                log.info("작업 완료 chunkIndex={} (size={})", currentChunkIndex, chunkData.size());
            });

            futures.add(future);
            chunkIndex++;
        }

        // 모든 작업 완료 대기
        for (Future<?> f : futures) {
            try {
                f.get(); // 여기서 블록
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("쓰레드 중단됨", e);
            } catch (ExecutionException e) {
                log.error("병렬 삽입에서 실행 에러 발생", e);
            }
        }

        executor.shutdown();
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        log.info("모든 chunk가 삽입됨 totalChunk={}", chunkIndex);
        log.info("삽입된 데이터의 개수: {}개, 소요 시간: {} ms", totalCount, totalTime);
    }

    public void insertLargeSellerDataParallel(int totalCount, int chunkSize, int threadCount) {
        log.info("대용량 데이터 병렬 삽입 시작 - totalCount={}, chunkSize={}, threadCount={}", totalCount, chunkSize, threadCount);
        long startTime = System.currentTimeMillis();
        // Iterator 로 Chunk 단위 Book 생성
        Iterator<List<Seller>> chunkIterator = dummyDataGenerator.generateSellerInChunks(totalCount, chunkSize);

        // 병렬 처리를 위한 ExecutorService
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        // 각 Chunk 마다 Submit
        int chunkIndex = 0;
        List<Future<?>> futures = new CopyOnWriteArrayList<>();

        while (chunkIterator.hasNext()) {
            final int currentChunkIndex = chunkIndex;
            final List<Seller> chunkData = chunkIterator.next();

            // 스레드 풀에 작업을 제출
            Future<?> future = executor.submit(() -> {
                // 스레드 내에서 독립적인 BATCH 세션을 열어서 Insert
                SqlSessionFactory sessionFactory = batchSqlSessionTemplate.getSqlSessionFactory();

                try (SqlSession session = sessionFactory.openSession(ExecutorType.BATCH)) {
                    SellerMapper mapper = session.getMapper(SellerMapper.class);
                    mapper.insertSellers(chunkData);

                    // flush + commit
                    session.flushStatements();
                    session.commit();
                } catch (Exception e) {
                    log.error("다음의 chunkIndex에서 오류 발생 chunkIndex={}", currentChunkIndex, e);
                }

                log.info("작업 완료 chunkIndex={} (size={})", currentChunkIndex, chunkData.size());
            });

            futures.add(future);
            chunkIndex++;
        }

        // 모든 작업 완료 대기
        for (Future<?> f : futures) {
            try {
                f.get(); // 여기서 블록
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("쓰레드 중단됨", e);
            } catch (ExecutionException e) {
                log.error("병렬 삽입에서 실행 에러 발생", e);
            }
        }

        executor.shutdown();
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        log.info("모든 chunk가 삽입됨 totalChunk={}", chunkIndex);
        log.info("삽입된 데이터의 개수: {}개, 소요 시간: {} ms", totalCount, totalTime);
    }
}
