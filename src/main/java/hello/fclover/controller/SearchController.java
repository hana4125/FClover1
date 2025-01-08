package hello.fclover.controller;

import hello.fclover.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping(value = "/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/detailSearch")
    public String detailSearch() {
        return "user/userDetailSearch";
    }


}
