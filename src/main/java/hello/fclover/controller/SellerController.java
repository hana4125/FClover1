package hello.fclover.controller;


import hello.fclover.domain.Seller;
import hello.fclover.service.SellerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(value="/seller")
public class SellerController {

    private final SellerService sellerService;

    @GetMapping("/main")
    public String signup() {
        return "seller/sellerMypage";
    }

    @GetMapping("/sellerSignup")
    public String sellerSignupForm() {
        return "seller/sellerSignup";
    }

    @PostMapping("/sellerSignup")
    public String sellerSignup(@ModelAttribute Seller seller) {

        sellerService.signup(seller);
        return "redirect:/";
    }

    @GetMapping("/sellerLogin")
    public String sellerLoginForm() {
        return "seller/sellerLogin";
    }

}
