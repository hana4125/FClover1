package hello.fclover.controller;

import hello.fclover.domain.Category;
import hello.fclover.domain.Goods;
import hello.fclover.domain.GoodsImage;
import hello.fclover.domain.Member;
import hello.fclover.dto.SearchDetailParamDTO;
import hello.fclover.dto.SearchParamDTO;
import hello.fclover.dto.SearchResponseDTO;
import hello.fclover.service.CategoryService;
import hello.fclover.service.GoodsService;
import hello.fclover.service.MemberService;
import hello.fclover.service.SearchService;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequestMapping(value = "/search")
@RequiredArgsConstructor
public class SearchController {

    // 의존성 주입
    private final SearchService searchService;
    private final CategoryService categoryService;
    private final MemberService memberService;
    private final GoodsService goodsService;

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
    public String keywordSearch(Model model, @RequestParam("keyword") String keyword) {

        SearchResponseDTO result = searchService.searchByKeyword(keyword);
        model.addAttribute("searchResult", result);
        return "user/userSearchResult";
    }


    @GetMapping("/searchDetail")
    public String detailSearch(Model model) {

        List<Category> categoryList = categoryService.getCategoryList();

        model.addAttribute("categoryList", categoryList);

        return "user/userSearchDetail";
    }

    // 상세 검색 기능
    // TODO : 검색 결과 관련 로직 전부 서비스 계층으로 옮기기
    @GetMapping("/searchDetailResult")
    public String searchDetailResult(Model model,
            SearchDetailParamDTO param,
            @RequestParam(value = "sort", required = false, defaultValue = "latest") String sort,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "20") int size) {

        int offset = (page - 1) * size;

        Map<String, Object> result = searchService.searchDetail(param, sort, offset, size);

        List<Goods> searchResults = (List<Goods>) result.get("searchResults");

        int totalCount = (int) result.get("totalCount");

        int totalPages = (int) Math.ceil((double) totalCount / size);

        int maxPageNumbersToShow = 10;
        int startPage;
        int endPage;

        if (totalPages <= maxPageNumbersToShow) {
            startPage = 1;
            endPage = totalPages;
        } else {
            if (page <= 6) {
                startPage = 1;
                endPage = 10;
            } else if (page + 4 >= totalPages) {
                startPage = totalPages - 9;
                endPage = totalPages;
            } else {
                startPage = page - 5;
                endPage = page + 4;
            }
        }

        model.addAttribute("searchResults", searchResults);
        model.addAttribute("keyword", param.getRepKeyword());
        model.addAttribute("sort", sort);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("size", size);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("totalCount", totalCount);

        return "user/userSearchResult";
    }

    @GetMapping("/refineAjax")
    public ResponseEntity<SearchResponseDTO> refineResultsAjax(@ModelAttribute SearchParamDTO param) {

        // 서비스 호출
        SearchResponseDTO result = searchService.refineResult(param);

        // JSON 으로 응답
        return ResponseEntity.ok(result);
    }

}