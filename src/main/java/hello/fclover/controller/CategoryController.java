package hello.fclover.controller;

import hello.fclover.domain.Category;
import hello.fclover.domain.Goods;
import hello.fclover.service.CategoryService;
import hello.fclover.service.GoodsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Slf4j
@Controller
@RequestMapping(value="/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final GoodsService goodsService;

    @GetMapping("/{no}")
    public String categoryDetail(@PathVariable("no") int cate_no, Model model) {

        // 카테고리 데이터 가져오기
        List<Category> categoryList = categoryService.getCategoryList();
        model.addAttribute("categoryList", categoryList);
        // 상품 목록 가져오기
        List<Goods> goodsList = goodsService.getGoodsList(cate_no);
        model.addAttribute("goodsList", goodsList);
        return "/user/userCategory"; // 카테고리 상세 페이지
    } // goodsDetail
}



