package hello.fclover.controller;

import hello.fclover.domain.*;
import hello.fclover.service.CategoryService;
import hello.fclover.service.GoodsService;
import hello.fclover.service.MemberService;
import hello.fclover.service.SellerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

        return "user/userHome";
    }

    @GetMapping("/error/403")
    public String error_403() {
        return "error/403";
    }
}
