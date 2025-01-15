package hello.fclover.controller;

import hello.fclover.domain.Goods;
import hello.fclover.domain.Seller;
import hello.fclover.service.GoodsService;
import hello.fclover.service.SellerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;


@Slf4j
@Controller
@RequestMapping(value = "/seller")
public class SellerController {
    private final SellerService sellerService;
    private final GoodsService goodsService;

    //    private final CategoryService categoryService;
    SellerController(SellerService sellerService, GoodsService goodsService) {
        this.sellerService = sellerService;
//        this.categoryService = categoryService;
        this.goodsService = goodsService;
    }

    @GetMapping("/addSingleProduct")
    public String addSingleProduct(Model model, Goods goods) {
        //회사이름, 사업자 등록번호(hidden)
//        String sellerCompany = sellerService.getCompanyName();
//        String sellerNumber sellerService.getSellerNumber();
//        model.addAttribute("sellerCompany", sellerCompany);
//        model.addAttribute("sellerNumber", sellerNumber);
        //category
//        List<Category> categoryList = categoryService.getCategoryList();
//        model.addAttribute("categoryList",categoryList);
        model.addAttribute("sellerCompany", "테스트 출판사");
        goods.setSellerId("testid");
        return "seller/sellerAddSingleProduct";
    }
    @PostMapping(value = "/addSingleProductProcess", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public String addSingleProductProcess(@RequestPart("goods") Goods goods,
            @RequestPart("goodsImages") List<MultipartFile> images) throws IOException {
        System.out.println("컨트롤단");
        for (MultipartFile file : images) {
            System.out.println("goods:" + file.getOriginalFilename());
        }
        goods.setSellerId("testid");
        String SellerNumber = "123-45-6789";
        System.out.println("goods:" + goods);
        goodsService.goodsSingleInsert(goods, images, SellerNumber);
        return "seller/sellerAddSingleProduct"; // 성공 후 상세 페이지로 이동
    }

    @GetMapping("/productDetail")
    public String productDetail(Model model) {
        return "seller/sellerProductDetail";
    }


    @GetMapping("/main")
    public String signup() {
        return "seller/sellerMypage";
    }

    @GetMapping("/sellerSignup")
    public String sellerSignup() {
        return "seller/sellerSignup";
    }

    @GetMapping("/sellerLogin")
    public String sellerLogin() {
        return "seller/sellerLogin";
    }

}
