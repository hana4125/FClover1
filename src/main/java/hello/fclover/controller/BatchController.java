package hello.fclover.controller;

import hello.fclover.domain.Member;
import hello.fclover.service.MemberService;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class BatchController {

    @Autowired
    private JobLauncher jobLauncher;

//    @Autowired
//    private Job importMemberJob;

//    @GetMapping("/runBatch")
//    public String runBatch() {
//        System.out.println("여기 runBatch" );
//        try {
//            System.out.println("여기 runbatch try문 내부");
//
//            jobLauncher.run(importMemberJob, new JobParameters());
//            return "Batch job started successfully!";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Failed to start batch job!";
//        }
//    }
}
