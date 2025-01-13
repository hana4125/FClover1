package hello.fclover.controller;


import hello.fclover.domain.Seller;
import hello.fclover.service.SellerService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/main")
    public String signup() {
        return "seller/sellerMain";
    }

    @GetMapping("/signup")
    public String sellerSignupForm() {
        return "seller/sellerSignup";
    }

    @PostMapping("/signup")
    public String sellerSignup(@ModelAttribute Seller seller) {

        String encPassword = passwordEncoder.encode(seller.getPassword());
        seller.setPassword(encPassword);
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
