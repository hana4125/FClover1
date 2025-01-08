package hello.fclover.controller;

import hello.fclover.domain.Category;
import hello.fclover.service.CategoryService;
import hello.fclover.domain.Member;
import hello.fclover.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.security.Principal;

@RequiredArgsConstructor
@Controller
public class HomeController {

    private final CategoryService categoryService;
    private final MemberService memberService;

    @ModelAttribute("member")
    public Member addMemberToModel(Principal principal) {

        if (principal != null) {
            String memberId = principal.getName();
            return memberService.findMemberById(memberId);
        }
        return null;
    }

    @GetMapping("/")
    public String home(Model model) {
        // 카테고리 데이터 가져오기
        List<Category> categoryList = categoryService.getCategoryList();
        model.addAttribute("categoryList", categoryList); // 모델에 데이터 추가
        return "user/userHome"; // Thymeleaf 템플릿 반환
    }
}
