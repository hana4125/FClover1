//package hello.fclover.task;
//
//import hello.fclover.domain.Payment;
//import hello.fclover.domain.Settlement;
//import hello.fclover.mybatis.mapper.PaymentMapper;
//import hello.fclover.mybatis.mapper.SettlementMapper;
//import hello.fclover.service.PaymentService;
//import lombok.extern.slf4j.Slf4j;
//import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Component;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ForkJoinPool;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Component
//public class SettlementScheduledTasks {
//    public static final String PAYMENT_COMPLETED = "paid";
//
//    private final PaymentMapper dao;
//
//    private final JdbcTemplate jdbcTemplate;
//
//    private final SettlementMapper settlementMapper;
//    private final PaymentMapper paymentMapper;
//
//    @Autowired
//    public SettlementScheduledTasks(PaymentMapper dao, JdbcTemplate jdbcTemplate, SettlementMapper settlementMapper, PaymentMapper paymentMapper) {
//        this.dao = dao;
//        this.jdbcTemplate = jdbcTemplate;
//        this.settlementMapper = settlementMapper;
//        this.paymentMapper = paymentMapper;
//    }
//
//    //@Scheduled(cron "0 * * * * ?")
//    @SchedulerLock(name = "ScheduledTask_run")
//    public void dailySettlement(){
//        LocalDate yesterday = LocalDate.now().minusDays(1);
//        LocalDateTime startDate = yesterday.atStartOfDay();
//        LocalDateTime endDate = yesterday.atTime(LocalTime.of(23,59,59));
//
//        //기간에 해당하는 결제내역 조회 및 집계
//        Map<Long, BigDecimal> settlementMap = getSettlementMap(startDate, endDate);
//
//        long beforeTime1 = System.currentTimeMillis(); //코드 실행후의 시간 받아오기
//
//        processSettlements(settlementMap,yesterday);
//
//        long afterTime1 = System.currentTimeMillis();
//
//        long diffTime1 = afterTime1-beforeTime1; //두개의 실행시간
//
//        log.info("실행시간(ms) : " + diffTime1); //세컨드 초단위 변환
//
//
//    }
//
//    private Map<Long, BigDecimal> getSettlementMap(LocalDateTime startDate, LocalDateTime endDate){
//        List<Payment> paymentList = paymentMapper.findByPaymentDateBetweenAndStatus(startDate, endDate, PAYMENT_COMPLETED);
//
//        return paymentList.stream()
//                .collect(Collectors.groupingBy(
//                        Payment::getPartnerId,
//                        Collectors.reducing(
//                                BigDecimal.ZERO,
//                                Payment::getPaymentAmount,
//                                BigDecimal::add
//                        )
//                ));
//
//    }
//
//
//    private void processSettlements(Map<Long, BigDecimal> settlementMap, LocalDate paymentDate){
//        ForkJoinPool customForkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors()); //ParrallelStream을 이용한 병렬처리
//
//        try{
//            customForkJoinPool.submit(() ->
//                    settlementMap.entrySet().parallelStream()
//                            .forEach(entry->{
//                                Settlement settlement = Settlement.create(entry.getKey(), entry.getValue(), paymentDate);
//                                settlementMapper.save(settlement);
//                            })
//                    )
//        }catch (Exception e){
//           e.printStackTrace();
//        }finally {
//            customForkJoinPool.shutdown();
//        }
//
//
//    }
//
//
//
//}
