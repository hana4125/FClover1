package hello.fclover.controller;


import hello.fclover.domain.Seller;
import hello.fclover.service.MemberService;
import hello.fclover.service.SellerService;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(value="/seller")
public class SellerController {

    private final MemberService memberService;
    private final SellerService sellerService;
    private final PasswordEncoder passwordEncoder;

    @ModelAttribute("seller")
    public Seller addSellerToModel(Principal principal) {

        if (principal != null) {
            String sellerId = principal.getName();
            return sellerService.findSellerById(sellerId);
        }
        return null;
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
    public String sellerSignup(HttpServletRequest request, RedirectAttributes redirectAttributes) {

        String sellerId = request.getParameter("sellerId");

        String memberIdDuplicate = memberService.isMemberIdDuplicate(sellerId);
        String sellerIdDuplicate = sellerService.isSellerIdDuplicate(sellerId);

        if (memberIdDuplicate != null || sellerIdDuplicate != null) {
            redirectAttributes.addFlashAttribute("message", "사용중인 아이디입니다.");
            return "redirect:/seller/signup";
        }

        //이유는 모르겠지만 ModelAttribute가 안됨
        Seller seller = new Seller();
        seller.setSellerId(sellerId);
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
