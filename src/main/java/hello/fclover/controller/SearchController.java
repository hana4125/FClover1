package hello.fclover.controller;

import hello.fclover.domain.Category;
import hello.fclover.domain.Goods;
import hello.fclover.domain.Member;
import hello.fclover.dto.SearchDetailForm;
import hello.fclover.service.CategoryService;
import hello.fclover.service.MemberService;
import hello.fclover.service.SearchService;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping(value = "/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;
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

    // 키워드 검색 기능
    @GetMapping("/searchKeyword")
    public ModelAndView keywordSearch(@RequestParam("keyword") String keyword,
            @RequestParam(value = "sort", required = false, defaultValue = "latest") String sort,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "20") int size) {

        ModelAndView mv = new ModelAndView();

        int totalCount = searchService.countByKeyword(keyword);

        int offset = (page - 1) * size;

        List<Goods> searchResults = searchService.searchByKeyword(keyword, sort, offset, size);

        int totalPages = (int) Math.ceil((double) totalCount / size);

        mv.setViewName("user/userSearchResult");
        mv.addObject("searchResults", searchResults);
        mv.addObject("keyword", keyword);
        mv.addObject("sort", sort);
        mv.addObject("currentPage", page);
        mv.addObject("totalPages", totalPages);
        mv.addObject("size", size);

        return mv;
    }


    @GetMapping("/searchDetail")
    public ModelAndView detailSearch() {

        ModelAndView mv = new ModelAndView();

        List<Category> categoryList = categoryService.getCategoryList();

        mv.setViewName("user/userSearchDetail");
        mv.addObject("categoryList", categoryList);

        return mv;
    }

    // 상세 검색 기능
    @GetMapping("/searchDetailResult")
    public ModelAndView searchDetailResult(@RequestParam(value = "") String cname) {

        ModelAndView mv = new ModelAndView();

        mv.setViewName("user/userSearchResult");

        return mv;
    }
}