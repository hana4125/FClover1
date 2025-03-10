package hello.fclover.controller;

import hello.fclover.domain.*;
import hello.fclover.service.CategoryService;
import hello.fclover.service.GoodsService;
import hello.fclover.service.MemberService;
import hello.fclover.service.SellerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class HomeController {

    private final CategoryService categoryService;
    private final MemberService memberService;
    private final SellerService sellerService;
    private final GoodsService goodsService;
//    private final Job importSellerJob;

//        @Autowired
//    private JobLauncher jobLauncher;
////
//    @Autowired
//    private Job importSellerJob;

    @ModelAttribute("member")
    public Member addMemberToModel(Principal principal) {

        if (principal != null) {
            String memberId = principal.getName();
            return memberService.findMemberById(memberId);
        }
        return null;
    }

    @ModelAttribute("seller")
    public Seller addSellerToModel(Principal principal) {

        if (principal != null) {
            String sellerId = principal.getName();
            return sellerService.findSellerById(sellerId);
        }
        return null;
    }

    @GetMapping("/")
    public String mainHome(Model model) {

        List<Category> categoryList = categoryService.getCategoryList();
        model.addAttribute("categoryList", categoryList);

        // 상품 목록 가져오기
        List<Goods> bestGoodsList = goodsService.getBestGoodsList(10);
        model.addAttribute("bestGoodsList", bestGoodsList);

        // 상품 목록 가져오기
        List<Goods> steadyGoodsList = goodsService.getSteadyGoodsList(10);
        model.addAttribute("steadyGoodsList", steadyGoodsList);

        // 대표 이미지 가져오기
        for (Goods goods : bestGoodsList) {
            GoodsImage mainImage = goodsService.getMainImageByGoodsNo(goods.getGoodsNo());
            goods.setMainImage(mainImage);
        }

        // 대표 이미지 가져오기
        for (Goods goods : steadyGoodsList) {
            GoodsImage mainImage = goodsService.getMainImageByGoodsNo(goods.getGoodsNo());
            goods.setMainImage(mainImage);
        }



//        System.out.println("여기 runBatch" );
//        try {
//            System.out.println("여기 runbatch try문 내부");
//
//            jobLauncher.run(importMemberJob, new JobParameters());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }


//        System.out.println("여기 runBatch" );
//        try {
//            System.out.println("여기 runbatch try문 내부");
//
//            jobLauncher.run(importSellerJob, new JobParameters());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }

        return "user/userHome";
    }

    @GetMapping("/error/403")
    public String error_403() {
        return "error/403";
    }


//    @Autowired
//    private JobLauncher jobLauncher;
//
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
