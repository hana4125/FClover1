package hello.fclover.config;

import hello.fclover.domain.Member;
import hello.fclover.mybatis.mapper.MemberMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private final MemberMapper memberMapper;


    public BatchConfig(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    // TaskExecutor 설정: 비동기 병렬 처리를 위한 스레드 풀
    @Bean
    public TaskExecutor taskExecutor() {
        System.out.println("taskExecutor()진입");
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(100);  // 너무 많은 스레드 생성
        taskExecutor.setMaxPoolSize(1000);  // 너무 많은 스레드 생성
        taskExecutor.setQueueCapacity(2000);
        taskExecutor.setThreadNamePrefix("batch-thread-");
        taskExecutor.initialize();
        return taskExecutor;
    }

//    // Job 정의
//    @Bean
//    public Job issueCouponsJob(JobRepository jobRepository, PlatformTransactionManager transactionManager, TaskExecutor taskExecutor) {
//        return new JobBuilder("issueCouponsJob", jobRepository)
//                .start(issueCouponsStep(jobRepository, transactionManager, taskExecutor))
//                .build();
//    }

    // Step 정의
    @Bean
    public Step issueCouponsStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, TaskExecutor taskExecutor) {
        return new StepBuilder("issueCouponsStep", jobRepository)
                .<Member, Member>chunk(1000000, transactionManager) // 1000개씩 처리
                .reader(memberReader()) // 데이터 읽기
                .processor(memberProcessor()) // 데이터 처리
                .writer(memberWriter()) // 데이터 쓰기
                .taskExecutor(taskExecutor) // 비동기 병렬 처리를 위한 TaskExecutor
                .build();
    }

    // ItemReader: "user"가 포함된 회원 목록 읽기
    @Bean
    public ItemReader<Member> memberReader() {
        System.out.println("멤버조회");
        // 여기서는 MemberMapper를 사용하여 "user"가 포함된 회원을 조회한다고 가정
        List<Member> members = memberMapper.findUsersWithUserInMemberId();
        System.out.println("멤버조회 완료");
        return new ListItemReader<>(members);
    }

    // ItemProcessor: 데이터를 처리하는 로직 (예: 쿠폰 발급)
    @Bean
    public ItemProcessor<Member, Member> memberProcessor() {
        return member -> {
            // 쿠폰 발급을 위한 추가 처리 로직을 넣을 수 있습니다.
            return member;
        };
    }

    // ItemWriter: 쿠폰 발급
    @Bean
    public ItemWriter<Member> memberWriter() {
        System.out.println("쿠폰발급");
        return members -> {
            for (Member member : members) {
//                Long memberId, String couponName, int couponAmount,LocalDateTime couponCreatedAt,LocalDateTime couponExpire
                // 회원에게 이벤트 쿠폰 발급
                memberMapper.insertCoupon(member.getMemberId(), "TEST_EVENT_COUPON", 2000);
                System.out.println("쿠폰 발급 완료: " + member.getMemberId());
            }
        };
    }



    //===========여기서부터는 Member 100만명 insert하는 batch 코드.
//    // Job 정의
//    @Bean
//    public Job importMemberJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
//        return new JobBuilder("importMemberJob", jobRepository)
//                .start(importMemberStep(jobRepository, transactionManager))
//                .build();
//    }
//
//    // Step 정의
//    @Bean
//    public Step importMemberStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
//        return new StepBuilder("importMemberStep", jobRepository)
//                .<Member, Member>chunk(1000, transactionManager) // 1000건씩 처리
//                .reader(memberReader()) // 데이터 읽기
//                .processor(memberProcessor()) // 데이터 처리
//                .writer(memberWriter()) // 데이터 쓰기
////                .taskExecutor(taskExecutor()) // TaskExecutor 설정 (멀티스레드 처리 활성화)
//                .build();
//    }
//
//
//    // ItemReader: 데이터를 읽는 역할
//    @Bean
//    public ItemReader<Member> memberReader() {
//        List<Member> members = new ArrayList<>();
//        for (int i = 0; i < 1000000; i++) {
//            members.add(new Member(null, "user" + i, "$2a$10$6vzd1jpjyMnJuhIk2yk3auMmAkQwuw45whQU7XPq9.c/ZjuCcaou2",
//                    "user " + i, "user" + i + "@example.com", "1990-01-01", "010-1234-5678", "M", "ROLE_USER", null, null, null));
//        }
//        return new ListItemReader<>(members);
//    }
//
//    // ItemProcessor: 데이터를 처리하는 역할
//    @Bean
//    public ItemProcessor<Member, Member> memberProcessor() {
//        return member -> {
//            member.setPassword("$2a$10$6vzd1jpjyMnJuhIk2yk3auMmAkQwuw45whQU7XPq9.c/ZjuCcaou2");
//            return member;
//        };
//    }
//
//    // ItemWriter: 데이터를 MySQL DB에 쓰는 역할
//    @Bean
//    public ItemWriter<Member> memberWriter() {
//        return items -> {
//            for (Member member : items) {
//                System.out.println("member = " + member);
//                memberMapper.insertMember(member);
//            }
//        };
//    }




}

