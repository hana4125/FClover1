package hello.fclover.controller;


import hello.fclover.domain.Member;
import hello.fclover.domain.Notice;
import hello.fclover.domain.PaginationResult;
import hello.fclover.domain.Question;
import hello.fclover.service.MemberService;
import hello.fclover.service.NoticeService;
import hello.fclover.service.QuestionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@Slf4j
@Controller
@RequestMapping(value="/inquiry")
@RequiredArgsConstructor
public class InquirycenterController {

    private final MemberService memberService;
    private final NoticeService noticeService;
    private final QuestionService questionService;


    @ModelAttribute("member")
    public Member addMemberToModel(Principal principal) {

        if (principal != null) {
            String memberId = principal.getName();
            return memberService.findMemberById(memberId);
        }
        return null;
    }

    //공지사항
    @GetMapping("/notice")
    public String notice(
            @RequestParam(defaultValue = "1") Integer page, Model m) {

        int limit = 10;
        int listcount = noticeService.getListCount();
        List<Notice> list = noticeService.getBoardList(page, limit);

        PaginationResult result = new PaginationResult(page, limit, listcount);
        m.addAttribute("page", page);
        m.addAttribute("maxpage", result.getMaxpage());
        m.addAttribute("startpage", result.getStartpage());
        m.addAttribute("endpage", result.getEndpage());
        m.addAttribute("listcount", listcount);
        m.addAttribute("noticelist", list);
        m.addAttribute("limit", limit);
        return "user/userNotice";
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/notice/write")
    public String noticeWrite() {
        return "user/userNoticeWrite";
    }

    @PostMapping(value ="/notice/add")
    public String noticeAdd(Notice notice) {
        noticeService.insertNotice(notice);
        return "redirect:/inquiry/notice";
    }

    @GetMapping(value = "/notice/detail")
    public ModelAndView Detail(
            int num, ModelAndView mv,
            HttpServletRequest request,
            String beforeURL, HttpSession session) {

        Notice notice = noticeService.getDetail(num);

        if (notice == null) {
            mv.setViewName("error/error");
            mv.addObject("url",request.getRequestURL());
            mv.addObject("message","상세보기 실패입니다.");
        }else {
            mv.setViewName("user/userNoticeDetail");
            mv.addObject("notidata", notice);
        }
        return mv;
    }

    //공지사항 검색
    @GetMapping(value = "/notice/noti_list")
    public ModelAndView noticeList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "3") int limit,
            ModelAndView mv,
            @RequestParam(defaultValue = "") String search_word)
    {
        System.out.println("페이지: " + page);
        System.out.println("검색어: " + search_word);

        int listcount = noticeService.getSearchListCount(search_word);
        List<Notice> list = noticeService.getSearchList( search_word, page, limit);
        PaginationResult result = new PaginationResult(page, limit, listcount);

        System.out.println("listcount = " + listcount);
        System.out.println("noticelist = " + list);

        mv.setViewName("user/userNotice");
        mv.addObject("page", page);
        mv.addObject("maxpage",result.getMaxpage());
        mv.addObject("startpage",result.getStartpage());
        mv.addObject("endpage",result.getEndpage());
        mv.addObject("noticelist",list);
        mv.addObject("search_word",search_word);
        mv.addObject("limit",limit);
        return mv;
    }

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
        return "user/userQNA";
    }

    @PostMapping(value ="/question/plus")
    public String noticeAdd(Question question) {
        questionService.insertQuestion(question);
        return "redirect:/inquiry/question";
    }

    @GetMapping(value = "/question/write")
    public String questionWrite() {
        return "user/userQNAWrite";
    }

    //질문보기
    @GetMapping(value = "/question/details")
    public ModelAndView questionDetail(
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
