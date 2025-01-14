package hello.fclover.controller;


import hello.fclover.domain.Notice;
import hello.fclover.domain.PaginationResult;
import hello.fclover.domain.Question;
import hello.fclover.service.QuestionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@Controller
@RequestMapping(value="/inquiry")
@RequiredArgsConstructor
public class InquirycenterController {

    private final QuestionService questionService;

    //문의사항
    @GetMapping("/question")
    public String question(
            @RequestParam(defaultValue = "1") Integer page, Model m) {

        int limit = 10;
        int listcount = questionService.ListCount();
        List<Question> list = questionService.BoardList(page, limit);

        PaginationResult result = new PaginationResult(page, limit, listcount);
        m.addAttribute("page", page);
        m.addAttribute("maxpage", result.getMaxpage());
        m.addAttribute("startpage", result.getStartpage());
        m.addAttribute("endpage", result.getEndpage());
        m.addAttribute("listcount", listcount);
        m.addAttribute("questionlist", list);
        m.addAttribute("limit", limit);
        return "user/userQnA";
    }

    @PostMapping(value ="/question/plus")
    public String noticeAdd(Question question) {
        questionService.insertQuestion(question);
        return "redirect:/inquiry/question";
    }

    //질문보기
    @GetMapping(value = "/question/details")
    public ModelAndView Detail(
            int no, ModelAndView mv,
            HttpServletRequest request,
            String beforeURL, HttpSession session) {

        Question question = questionService.Detail(no);

        if (question == null) {
            mv.setViewName("error/error");
            mv.addObject("url",request.getRequestURL());
            mv.addObject("message","상세보기 실패입니다.");
        }else {
            mv.setViewName("user/userQNADetail");
            mv.addObject("questiondata", question);
        }
        return mv;
    }
}
