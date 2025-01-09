package hello.fclover.controller;

import hello.fclover.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping(value = "/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/searchDetail")
    public String detailSearch() {
        return "user/userSearchDetail";
    }

    @GetMapping("/searchDetailResult")
    public ModelAndView searchDetailResult(
            String cname, String chrcDetail, String pbcmDetail, String repKeyword, String cate, String repKeywordTarget
    ) {
        ModelAndView mv = new ModelAndView();

        mv.setViewName("user/userSearchResult");
        return mv;
    }
}
