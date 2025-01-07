package hello.fclover.controller;


import hello.fclover.domain.Goods;
import hello.fclover.service.SellerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Slf4j
@Controller
@RequestMapping(value="/seller")
public class SellerController {

    private final SellerService sellerService;

    SellerController(SellerService sellerService) {
        this.sellerService = sellerService;

    }

    @GetMapping("/addSingleProduct")
    public String addSingleProduct(Model model) {
        return "seller/sellerAddSingleProduct";
    }

    @PostMapping("/addSingleProductProcess")
    public String addSingleProductProcess(Goods goods,
                                          HttpServletRequest request,
                                          @RequestParam("goods_image") List<MultipartFile> files) {
        sellerService.goodsSingleInsert(goods);


            for (MultipartFile file : files) {
                // 파일 이름 가져오기
                String fileName = file.getOriginalFilename();

                System.out.println(file);
            }


        return "seller/sellerAddSingleProduct"; // 성공 후 상세 페이지로 이동
    }

    @GetMapping("/productDetail")
    public String productDetail(Model model) {
        return "seller/sellerProductDetail";
    }


}
