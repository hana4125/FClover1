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
                                          @RequestParam("goods_image") MultipartFile[] files,
                                          Model model) {


        // 3. 결과 모델에 추가
        model.addAttribute("message", "상품이 성공적으로 등록되었습니다!");

        // 4. 성공 페이지로 리다이렉트 또는 결과 반환
        return "redirect:/seller/productDetail"; // 성공 후 상세 페이지로 이동
    }

    @GetMapping("/productDetail")
    public String productDetail(Model model) {
        return "seller/sellerProductDetail";
    }


}
