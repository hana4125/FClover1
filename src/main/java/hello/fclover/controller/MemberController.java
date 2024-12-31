package hello.fclover.controller;

import hello.fclover.domain.Member;
import hello.fclover.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/signup")
    public String signup() {
        return "user/userSignup";
    }

    @PostMapping("/signupProcess")
    public String signupProcess(@ModelAttribute Member member) {

        log.info("auth={}", member.getAuth());
        String encPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encPassword);

        int result = memberService.signup(member);

        if (result == 1) {
            log.info("회원가입 완료");
            return "redirect:/";
        } else {
            log.info("회원가입 실패");
            return "error/error";
        }
    }

    @GetMapping("/login")
    public String login(@CookieValue(required = false) String rememberId, HttpSession session, Model model) {

        if (rememberId != null) {
            log.info("rememberId={}", rememberId);
            model.addAttribute("rememberId", rememberId);
        }

        model.addAttribute("message", session.getAttribute("loginfail"));
        session.removeAttribute("loginfail");

        return "user/userLogin";
    }

    @GetMapping("/myPage")
    public String myPageMain() {
        return "/user/mypage/userMyPageMain";
    }

    @GetMapping("/myPage/deliveryAddressBook")
    public String myPageDeliveryAddressBook() {
        return "/user/mypage/userMyPageDeliveryAddressBook";
    }

    @GetMapping("/myPage/info")
    public String myPageInfo() {
        return "/user/mypage/userMyPageInfo";
    }

    @GetMapping("/myPage/orderDelivery")
    public String myPageOrderDelivery() {
        return "/user/mypage/userMyPageOrderDelivery";
    }

    @GetMapping("/myPage/purchaseHistory")
    public String myPagePurchaseHistory() {
        return "/user/mypage/userMyPagePurchaseHistory";
    }

    @GetMapping("/myPage/wishlist")
    public String myPageWishlist() {
        return "/user/mypage/userMyPageWishlist";
    }

    @GetMapping("/cart")
    public String cart() {
        return "/user/userCart";
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
