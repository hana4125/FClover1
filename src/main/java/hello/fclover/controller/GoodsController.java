package hello.fclover.controller;


import hello.fclover.domain.Category;
import hello.fclover.domain.Goods;
import hello.fclover.domain.GoodsImage;
import hello.fclover.domain.Member;
import hello.fclover.service.CategoryService;
import hello.fclover.service.GoodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/goods")
@RequiredArgsConstructor
public class GoodsController {
    private final GoodsService goodsService;
    private final CategoryService categoryService;

    @GetMapping("/GoodsDetail/{no}")
    public String goodsDetail(Model model, @PathVariable("no") Long goodsNo) {
        goodsService.getGoodsDetail(goodsNo, model);
        System.out.println("============================================");
        System.out.println("model = " + model.getAttribute("imageList"));
        return "user/userGoodsDetail";
    }
   /* @PostMapping("/SearchGoodsProcess")
    public List<Goods> searchGoodsProcess(@ModelAttribute Goods goods) {
        List<Goods> list = new ArrayList<>();
        return list;
    }*/

    @GetMapping("/category/{no}")
    public String categoryDetail(@PathVariable("no") int cateNo,
                                 @ModelAttribute("member") Member member,
                                 @RequestParam(value = "sort", required = false, defaultValue = "latest") String sort,
                                 @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                 @RequestParam(value = "size", required = false, defaultValue = "20") int size,
                                 Model model) {

        // 카테고리 데이터 가져오기
        List<Category> categoryList = categoryService.getCategoryList();
        model.addAttribute("categoryList", categoryList);

        // 현재 선택된 카테고리 객체 가져오기
        Category selectedCategory = categoryService.getCategoryByNo(cateNo);
        model.addAttribute("category", selectedCategory);

        // 회원 번호 가져오기
        Long memberNo = null;
        if (member != null) {
            memberNo = member.getMemberNo();
        }

        // 찜 상태가 포함된 상품 목록 조회
        List<Goods> goodsList = goodsService.getGoodsWithWishStatusList(memberNo, cateNo, sort, page, size);
        model.addAttribute("goodsList", goodsList);

        // 대표 이미지 가져오기
        for (Goods goods : goodsList) {
            GoodsImage mainImage = goodsService.getMainImageByGoodsNo(goods.getGoodsNo());
            goods.setMainImage(mainImage);
        }

        // 페이지네이션 정보 전달
        int totalItems = goodsService.getTotalGoodsCount(cateNo);
        int totalPages = (int) Math.ceil((double) totalItems / size);

        int maxPageNumbersToShow = 10;
        int startPage;
        int endPage;

        if (totalPages <= maxPageNumbersToShow) {
            startPage = 1;
            endPage = totalPages;
        } else {
            if (page <= 6) {
                startPage = 1;
                endPage = 10;
            } else if (page + 4 >= totalPages) {
                startPage = totalPages - 9;
                endPage = totalPages;
            } else {
                startPage = page - 5;
                endPage = page + 4;
            }
        }

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("sort", sort);
        model.addAttribute("size", size);
        return "/user/userCategory"; // 카테고리 상세 페이지
    }

    @GetMapping("/bestSeller")
    public String bestSeller(@ModelAttribute("member") Member member,
                             @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                             @RequestParam(value = "size", required = false, defaultValue = "20") int size,
                             Model model) {
        return prepareSellerPage(member, page, size, model, "/user/userBestseller");
    }

    @GetMapping("/steadySeller")
    public String steadySeller(@ModelAttribute("member") Member member,
                               @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                               @RequestParam(value = "size", required = false, defaultValue = "20") int size,
                               Model model) {
        return prepareSellerPage(member, page, size, model, "/user/userSteadyseller");
    }

    private String prepareSellerPage(Member member, int page, int size, Model model, String viewName) {
        // 카테고리 데이터 가져오기
        List<Category> categoryList = categoryService.getCategoryList();
        model.addAttribute("categoryList", categoryList);

        // 회원 번호 가져오기
        Long memberNo = null;
        if (member != null) {
            memberNo = member.getMemberNo();
        }

        // 찜 상태가 포함된 상품 목록 조회
        List<Goods> bestGoodsList = goodsService.getBestGoodsWishStatus(memberNo, page, size);
        model.addAttribute("bestGoodsList", bestGoodsList);

        // 대표 이미지 가져오기
        for (Goods goods : bestGoodsList) {
            GoodsImage mainImage = goodsService.getMainImageByGoodsNo(goods.getGoodsNo());
            goods.setMainImage(mainImage);
        }

        // 찜 상태가 포함된 상품 목록 조회
        List<Goods> steadyGoodsList = goodsService.getSteadyGoodsWishStatus(memberNo, page, size);
        model.addAttribute("steadyGoodsList", steadyGoodsList);

        // 대표 이미지 가져오기
        for (Goods goods : steadyGoodsList) {
            GoodsImage mainImage = goodsService.getMainImageByGoodsNo(goods.getGoodsNo());
            goods.setMainImage(mainImage);
        }

        // 페이지네이션 정보 전달
        int totalItems = Math.min(goodsService.getTotalBestGoodsCount(memberNo), 100);
        int totalPages = (int) Math.ceil((double) totalItems / size);

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("size", size);
        return viewName;
    }

    @GetMapping("/newItems")
    public String NewItems(@ModelAttribute("member") Member member,
                           @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                           @RequestParam(value = "size", required = false, defaultValue = "20") int size,
                           @RequestParam(value = "year", required = false) String year,
                           @RequestParam(value = "month", required = false) String month,
                           @RequestParam(value = "week", required = false) String week,
                           Model model) {

        // 카테고리 데이터 가져오기
        List<Category> categoryList = categoryService.getCategoryList();
        model.addAttribute("categoryList", categoryList);

        // 회원 번호 가져오기
        Long memberNo = null;
        if (member != null) {
            memberNo = member.getMemberNo();
        }

        // 신상품 목록 조회: 정렬 방식과 일자 필터 적용
        List<Goods> goodsList = goodsService.getNewItems(memberNo, year, month, week, page, size);
        model.addAttribute("goodsList", goodsList);

        // 대표 이미지 가져오기
        for (Goods goods : goodsList) {
            GoodsImage mainImage = goodsService.getMainImageByGoodsNo(goods.getGoodsNo());
            goods.setMainImage(mainImage);
        }

        // 페이지네이션 정보 전달
        int actualTotalItems = goodsService.getTotalNewItemsCount(memberNo, year, month, week);
        int totalItems = Math.min(actualTotalItems, 100); // 최대 100개로 제한
        int totalPages = (int) Math.ceil((double) totalItems / size);

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("size", size);
        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("week", week);

        return "/user/userNewItems";
    }
}
