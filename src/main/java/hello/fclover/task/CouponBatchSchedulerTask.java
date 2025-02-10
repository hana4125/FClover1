package hello.fclover.task;


import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CouponBatchSchedulerTask {
//    private final JobLauncher jobLauncher;
//    private final Job issueCouponsJob;
//
//    public CouponBatchSchedulerTask(JobLauncher jobLauncher, Job issueCouponsJob) {
//        this.jobLauncher = jobLauncher;
//        this.issueCouponsJob = issueCouponsJob;
//    }
//
//    // 매일 2월 8일 22:30에 배치 작업 실행
//    @Scheduled(cron = "0 16 21 9 2 *") // 2월 8일 22:30
//    public void runJob() {
//        try {
//            System.out.println("쿠폰발급작업 시작");
//            jobLauncher.run(issueCouponsJob, new JobParameters());
//            System.out.println("쿠폰 발급 배치 작업완료");
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("쿠폰 발급 배치 작업 실패");
//        }
//    }

}
