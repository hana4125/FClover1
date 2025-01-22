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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/notice/write")
    public String noticeWrite() {
        return "user/userNoticeWrite";
    }

    @PostMapping(value ="/notice/add")
    public String noticeAdd(Notice notice) {
        noticeService.insertNotice(notice);
        return "redirect:/inquiry/notice/noti_list";
    }

    //공지사항 보기
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
            @RequestParam(defaultValue = "10") int limit,
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
        mv.addObject("listcount",listcount);
        return mv;
    }

    //문의사항
    @GetMapping("/question")
    public String question(
            @RequestParam(defaultValue = "1") Integer currentPage, Model m) {

        int limit = 10;
        int totalcount = questionService.TotalCount();
        List<Question> questionlist = questionService.BoardList(currentPage, limit);

        PaginationResult rt = new PaginationResult(currentPage, limit, totalcount);
        m.addAttribute("currentPage", currentPage);
        m.addAttribute("maxpage", rt.getMaxpage());
        m.addAttribute("startpage", rt.getStartpage());
        m.addAttribute("endpage", rt.getEndpage());
        m.addAttribute("totalcount", totalcount);
        m.addAttribute("questionlist", questionlist);
        m.addAttribute("limit", limit);
        return "user/userQNA";
    }

    @GetMapping(value = "/question/write")
    public String questionWrite() {
        return "user/userQNAWrite";
    }

    @PostMapping(value = "/question/plus")
    public String noticeAdd(Question question, @RequestParam(required = false) String qalert) throws Exception {
        // qalert 값이 null일 경우 "n"으로 설정
        if (qalert == null || (!qalert.equalsIgnoreCase("y") && !qalert.equalsIgnoreCase("n"))) {
            question.setQalert("n");
        } else {
            question.setQalert(qalert.equalsIgnoreCase("y") ? "y" : "n");
        }

        MultipartFile uploadfile = question.getUploadfile();
        if (!uploadfile.isEmpty()) {
            String fileDBName = questionService.saveUploadFile(uploadfile, "D://temp");
            question.setQfile(fileDBName); //바뀐 파일명으로 저장

        }

        questionService.insertQuestion(question);
        return "redirect:/inquiry/question";
    }

    //문의사항 보기
    @GetMapping(value = "/question/detail")
    public ModelAndView questionDetail(
            int no, ModelAndView mv,
            HttpServletRequest request,
            String beforeURL, HttpSession session) {

        Question question = questionService.Detail(no);
        String qValue = questionService.getQvalue(question.getQtype());

        if (question == null) {
            mv.setViewName("error/error");
            mv.addObject("url",request.getRequestURL());
            mv.addObject("message","상세보기 실패입니다.");
        }else {
            mv.setViewName("user/userQNADetail");
            mv.addObject("questiondata", question);
            mv.addObject("qValue", qValue);

        }
        return mv;
    }



    //문의사항 댓글
    @PostMapping(value = "/qlist")
    public Map<String, Object> CommentList(int board_num, int page) {
        List<Question> qlist = questionService.getCommentList(board_num, page);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("qlist", qlist);

        return map;
    }

    @PostMapping(value = "/add")
    public int commentAdd(Question c) {
        return questionService.commentsInsert(c);
    }

    @PostMapping(value = "/update")
    public int commentUpdate(Question co) {
        return questionService.commentsUpdate(co);
    }

    @PostMapping(value = "/delete")
    public int commentDelete(int num) {
        return questionService.commentDelete(num);
    }














}


