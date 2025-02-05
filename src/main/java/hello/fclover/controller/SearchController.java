package hello.fclover.controller;

import hello.fclover.domain.Category;
import hello.fclover.domain.Member;
import hello.fclover.dto.SearchDetailParamDTO;
import hello.fclover.dto.SearchParamDTO;
import hello.fclover.dto.SearchResponseDTO;
import hello.fclover.service.CategoryService;
import hello.fclover.service.MemberService;
import hello.fclover.service.SearchService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping(value = "/search")
@RequiredArgsConstructor
public class SearchController {

    // 의존성 주입
    private final SearchService searchService;
    private final CategoryService categoryService;
    private final MemberService memberService;

    // 멤버 로그인 정보
    @ModelAttribute("member")
    public Member addMemberToModel(Principal principal) {

        if (principal != null) {
            String memberId = principal.getName();
            return memberService.findMemberById(memberId);
        }

        return null;
    }


    // 키워드 검색
    @GetMapping("/searchKeyword")
    public String keywordSearch(Model model,
            @RequestParam("keyword") String keyword,
            HttpServletRequest request,
            @ModelAttribute("member") Member member) {

        // 세션 아이디 가져오기
        HttpSession session = request.getSession();
        String sessionId = session.getId();

        SearchResponseDTO result = searchService.searchByKeyword(keyword, sessionId, member);

        model.addAttribute("searchResult", result);

        return "user/userSearchResult";
    }


    // 상세 검색 페이지
    @GetMapping("/searchDetail")
    public String detailSearch(Model model) {

        List<Category> categoryList = categoryService.getCategoryList();
        model.addAttribute("categoryList", categoryList);

        return "user/userSearchDetail";
    }


    // 상세 검색 결과
    @GetMapping("/searchDetailResult")
    public String searchDetailResult(Model model, SearchDetailParamDTO param) {

        SearchResponseDTO result = searchService.searchDetail(param);
        model.addAttribute("searchResult", result);

        return "user/userSearchResult";
    }


    // 검색 필터링 Ajax
    @GetMapping("/refineAjax")
    public ResponseEntity<SearchResponseDTO> refineResultsAjax(@ModelAttribute SearchParamDTO param) {

        // 서비스 호출
        SearchResponseDTO result = searchService.refineResult(param);

        // JSON 으로 응답
        return ResponseEntity.ok(result);
    }

}