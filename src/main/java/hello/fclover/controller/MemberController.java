package hello.fclover.controller;

import hello.fclover.domain.Member;
import hello.fclover.domain.Payment;
import hello.fclover.domain.PaymentReq;
import hello.fclover.service.MemberService;
import hello.fclover.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final PaymentService paymentService;

    @GetMapping("/signup")
    public String signup() {
        return "user/userSignup";
    }

    @PostMapping("/signupProcess")
    public String signupProcess(@ModelAttribute Member member,
                                RedirectAttributes rattr,
                                Model model,
                                HttpServletRequest request) {

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
    public String myPage() {
        return "user/userMyPage";
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

    @GetMapping("/memberPay")
    public String sellerPay() {
        return "user/userPayments";
    }

    @GetMapping("/memberPayDone")
    public String sellerPayDone() {
        return "user/userPaymentsDone";
    }

    @GetMapping("/memberOrderList")
    public String OrderList() {
        return "user/userOrderList";
    }


    @PostMapping("/portone")
    public ResponseEntity<Map<String, String>> savePortone(@RequestBody PaymentReq paymentRequest) {
        Map<String, String> response = new HashMap<>();
        System.out.println("========>controller의 paymentRequest : " + paymentRequest);

        try {
            System.out.println("========>controller의 try문 안의 paymentRequest : " + paymentRequest);
            System.out.println("========>controller의 try문 안의 paymentService.savePayment(Payment.save(paymentRequest)) : " + paymentService.savePayment(Payment.save(paymentRequest)));

            paymentService.savePayment(Payment.save(paymentRequest));

            System.out.println("========Controller====>Payment.save(paymentRequest)" + Payment.save(paymentRequest));
            response.put("message", "Payment processed successfully.");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Failed to process payment.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
