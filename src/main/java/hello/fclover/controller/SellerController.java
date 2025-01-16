package hello.fclover.controller;


import hello.fclover.domain.Goods;
import hello.fclover.domain.Seller;
import hello.fclover.service.GoodsService;
import hello.fclover.service.SellerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.security.Principal;
import java.io.IOException;
import java.util.List;


@Slf4j
@Controller

@RequiredArgsConstructor
@RequestMapping(value="/seller")
public class SellerController {
    private final SellerService sellerService;
    private final GoodsService goodsService;
    private final PasswordEncoder passwordEncoder;

    @ModelAttribute("seller")
    public Seller addSellerToModel(Principal principal) {

        if (principal != null) {
            String sellerId = principal.getName();
            return sellerService.findSellerById(sellerId);
        }
        return null;
    }
@GetMapping("/addSingleProduct")
    public String addSingleProduct(Model model, Goods goods) {

        return "seller/sellerAddSingleProduct";
    }
    @PostMapping(value = "/addSingleProductProcess", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public String addSingleProductProcess(@RequestPart("goods") Goods goods,
            @RequestPart("goodsImages") List<MultipartFile> images,
                                          Principal principal) throws IOException {
        System.out.println("컨트롤단");
        for (MultipartFile file : images) {
            System.out.println("goods:" + file.getOriginalFilename());
        }

        String sellerId = principal.getName();
        Seller seller = sellerService.findSellerById(sellerId);
        String businessNumber = seller.getBusinessNumber();

        System.out.println("goods:" + goods);
        goodsService.goodsSingleInsert(goods, images, businessNumber);
        return "seller/sellerAddSingleProduct"; // 성공 후 상세 페이지로 이동

    }
    @GetMapping("/productDetail")
    public String productDetail(Model model) {
        return "seller/sellerProductDetail";
    }


    @GetMapping("/main")
    public String signup(Principal principal) {

        if (principal == null) {
            return "redirect:/seller/login";
        }

        return "seller/sellerMain";
    }

    @GetMapping("/signup")
    public String sellerSignupForm() {
        return "seller/sellerSignup";
    }

    @PostMapping("/signupProcess")
    public String sellerSignup(HttpServletRequest request) {

        //이유는 모르겠지만 ModelAttribute가 안됨
        Seller seller = new Seller();
        seller.setSellerId(request.getParameter("sellerId"));
        seller.setPassword(passwordEncoder.encode(request.getParameter("password")));
        seller.setName(request.getParameter("name"));
        seller.setEmail(request.getParameter("email"));
        seller.setPhoneNumber(request.getParameter("phoneNumber"));
        seller.setBusinessNumber(request.getParameter("businessNumber"));
        seller.setCompanyName(request.getParameter("companyName"));

        sellerService.signup(seller);
        return "redirect:/seller/main";
    }

    @GetMapping("/login")
    public String sellerLoginForm(HttpSession session, Model model) {

        model.addAttribute("message", session.getAttribute("sellerLoginfail"));
        session.removeAttribute("sellerLoginfail");

        return "seller/sellerLogin";
    }
}
