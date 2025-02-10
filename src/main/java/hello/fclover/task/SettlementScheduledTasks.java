package hello.fclover.task;

import hello.fclover.domain.Payment;
import hello.fclover.domain.Settlement;
import hello.fclover.mybatis.mapper.PaymentMapper;
import hello.fclover.mybatis.mapper.SettlementMapper;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.apache.ibatis.session.SqlSession;
//import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SettlementScheduledTasks {
    public static final String PAYMENT_COMPLETED = "paid";

    private final PaymentMapper paymentMapper;

    private final JdbcTemplate jdbcTemplate;

    private final SettlementMapper settlementMapper;

    @Autowired
    public SettlementScheduledTasks(PaymentMapper paymentMapper, JdbcTemplate jdbcTemplate, SettlementMapper settlementMapper) {
        this.paymentMapper = paymentMapper;
        this.jdbcTemplate = jdbcTemplate;
        this.settlementMapper = settlementMapper;
    }

    @Transactional
    @Scheduled(cron = "0 1 0 * * ?")
    @SchedulerLock(name = "ScheduledTask_run")
    public void dailySettlement(){
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDateTime startDate = yesterday.atStartOfDay();
        LocalDateTime endDate = yesterday.atTime(LocalTime.of(23,59,59));

        System.out.println("======>정산 스케쥴링 실행 ");
        //기간에 해당하는 결제내역 조회 및 집계
        Map<Long, BigDecimal> settlementMap = getSettlementMap(startDate, endDate);

        long beforeTime1 = System.currentTimeMillis(); //코드 실행후의 시간 받아오기

        processSettlements(settlementMap,yesterday);

        long afterTime1 = System.currentTimeMillis();

        long diffTime1 = afterTime1-beforeTime1; //두개의 실행시간

        log.info("실행시간(ms) : " + diffTime1); //세컨드 초단위 변환


    }

    private Map<Long, BigDecimal> getSettlementMap(LocalDateTime startDate, LocalDateTime endDate){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String formattedStartDate = startDate.format(formatter);
        String formattedEndDate = endDate.format(formatter);

        List<Payment> paymentList = paymentMapper.findByPaymentDateBetweenAndStatus(formattedStartDate, formattedEndDate, PAYMENT_COMPLETED);
        System.out.println("paymentList = " + paymentList);

        return paymentList.stream()
                .collect(Collectors.groupingBy(
                        Payment::getSellerNo,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Payment::getPaymentAmount,
                                BigDecimal::add
                        )
                ));

    }


    private int processSettlements(Map<Long, BigDecimal> settlementMap, LocalDate paymentDate){
        ForkJoinPool customForkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors()); //ParrallelStream을 이용한 병렬처리

        int result = 0;
        try{
            customForkJoinPool.submit(() ->
                    settlementMap.entrySet().parallelStream()
                            .forEach(entry->{
                                Settlement settlement = Settlement.create(entry.getKey(), entry.getValue(), paymentDate);
                                settlementMapper.save(settlement);


                            })

                    );
            result++;
        }catch (Exception e){
           e.printStackTrace();
        }finally {
            customForkJoinPool.shutdown();
        }

            return result;
    }



}
