package hello.fclover.controller;

import hello.fclover.domain.Category;
import hello.fclover.domain.Goods;
import hello.fclover.domain.GoodsImage;
import hello.fclover.domain.Member;
import hello.fclover.dto.GoodsSearchParam;
import hello.fclover.service.CategoryService;
import hello.fclover.service.GoodsService;
import hello.fclover.service.MemberService;
import hello.fclover.service.SearchService;
import java.security.Principal;
import java.util.List;
import java.util.Map;
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
    public ModelAndView keywordSearch(@RequestParam("keyword") String keyword,
            @ModelAttribute("member") Member member,
            @RequestParam(value = "sort", required = false, defaultValue = "latest") String sort,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "20") int size) {

        ModelAndView mv = new ModelAndView();

        int offset = (page - 1) * size;

        Map<String, Object> result = searchService.searchByKeyword(keyword, sort, offset, size);

        int totalCount = (int) result.get("totalCount");

        int totalPages = (int) Math.ceil((double) totalCount / size);

        List<Goods> searchResults = (List<Goods>) result.get("searchResults");

        // TODO : 찜 상태는 완성되면 추가


        // 대표 이미지 가져오기
        for (Goods goods : searchResults) {
            GoodsImage mainImage = goodsService.getMainImageByGoodsNo(goods.getGoodsNo());
            goods.setMainImage(mainImage);
        }

        // TODO : 중복되는 코드 메소드화 하거나 유틸 클래스로 빼기
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

        mv.setViewName("user/userSearchResult");
        mv.addObject("searchResults", searchResults);
        mv.addObject("keyword", keyword);
        mv.addObject("totalCount", totalCount);

        mv.addObject("sort", sort);
        mv.addObject("currentPage", page);
        mv.addObject("totalPages", totalPages);
        mv.addObject("startPage", startPage);
        mv.addObject("endPage", endPage);
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
    public ModelAndView searchDetailResult(GoodsSearchParam param,
            @RequestParam(value = "sort", required = false, defaultValue = "latest") String sort,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "20") int size) {
        ModelAndView mv = new ModelAndView();

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

        mv.setViewName("user/userSearchResult");
        mv.addObject("searchResults", searchResults);
        mv.addObject("keyword", param.getRepKeyword());
        mv.addObject("sort", sort);
        mv.addObject("currentPage", page);
        mv.addObject("totalPages", totalPages);
        mv.addObject("size", size);
        mv.addObject("startPage", startPage);
        mv.addObject("endPage", endPage);
        mv.addObject("totalCount", totalCount);

        return mv;
    }
}