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

    @GetMapping("/")
    public ModelAndView simpleSearch(String keyword) {
        ModelAndView mv = new ModelAndView();

        mv.setViewName("user/userSearch");
        mv.addObject("keyword", keyword);
        return mv;
    }

}
