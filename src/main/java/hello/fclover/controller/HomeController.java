package hello.fclover.controller;

import hello.fclover.domain.Category;
import hello.fclover.domain.Seller;
import hello.fclover.service.CategoryService;
import hello.fclover.domain.Member;
import hello.fclover.service.MemberService;
import hello.fclover.service.SellerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@Controller
public class HomeController {

    private final CategoryService categoryService;
    private final MemberService memberService;
    private final SellerService sellerService;

    @ModelAttribute("member")
    public Member addMemberToModel(Principal principal) {

        if (principal != null) {
            String memberId = principal.getName();
            return memberService.findMemberById(memberId);
        }
        return null;
    }

    @ModelAttribute("seller")
    public Seller addSellerToModel(Principal principal) {

        if (principal != null) {
            String sellerId = principal.getName();
            return sellerService.findSellerById(sellerId);
        }
        return null;
    }

    @GetMapping("/")
    public String home(Model model) {
        return "redirect:/member/main";
    }

    @GetMapping("/member/main")
    public String mainHome(Model model) {
        List<Category> categoryList = categoryService.getCategoryList();
        model.addAttribute("categoryList", categoryList);
        return "user/userHome";
    }
}
