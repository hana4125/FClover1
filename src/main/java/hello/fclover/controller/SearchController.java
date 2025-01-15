package hello.fclover.controller;

import hello.fclover.domain.Goods;
import hello.fclover.dto.SearchDetailForm;
import hello.fclover.service.SearchService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping(value = "/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    // 키워드 검색 기능
    @GetMapping
    public ModelAndView keywordSearch(@RequestParam("keyword") String keyword) {

        ModelAndView mv = new ModelAndView();
        List<Goods> searchResults = searchService.searchByKeyword(keyword);

        mv.setViewName("user/userSearchResult");
        mv.addObject("searchResults", searchResults);
        mv.addObject("keyword", keyword);

        return mv;
    }


    @GetMapping("/searchDetail")
    public String detailSearch() {
        return "user/userSearchDetail";
    }


    // 상세 검색 기능
//    @GetMapping("/searchDetailResult")
//    public ModelAndView searchDetailResult(SearchDetailForm searchDetailForm) {
//
//        ModelAndView mv = new ModelAndView();
//        List<Goods> searchDetailResult = searchService.searchDetail(searchDetailForm);
//
//        mv.setViewName("user/userSearchResult");
//        mv.addObject("searchDetailForm", searchDetailForm);
//        mv.addObject("searchDetailResult", searchDetailResult);
//
//        return mv;
//    }
}