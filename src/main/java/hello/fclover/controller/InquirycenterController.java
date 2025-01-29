package hello.fclover.controller;
import hello.fclover.domain.Member;
import hello.fclover.domain.Notice;
import hello.fclover.domain.PaginationResult;
import hello.fclover.domain.Question;
import hello.fclover.mail.EmailMessage;
import hello.fclover.mail.EmailService;
import hello.fclover.service.MemberService;
import hello.fclover.service.NoticeService;
import hello.fclover.service.QuestionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    private final EmailService emailService;

    @ModelAttribute("member")
    public Member addMemberToModel(Principal principal) {

        if (principal != null) {
            String memberId = principal.getName();
            return memberService.findMemberById(memberId);
        }
        return null;
    }

    //공지사항 작성(관리자만)
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/notice/write")
    public String noticeWrite() {
        return "user/userNoticeWrite";
    }

    //공지사항 등록
    @PostMapping(value ="/notice/add")
    public String noticeAdd(Notice notice, Principal principal) {
        notice.setNotiname(principal.getName());
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

    //공지사항 삭제
    @PostMapping(value = "/notice/delete")
    public String deleteNotice(@RequestBody Map<String, Integer> body,
                               RedirectAttributes rattr,
                               Principal principal) {
        int notino = body.get("num");

        // 관리자 권한 체크
        if (!principal.getName().equals("admin")) {
            rattr.addFlashAttribute("errorMessage", "삭제 권한이 없습니다.");
            return "redirect:/inquiry/notice/detail?num=" + notino;
        }

        System.out.println("Delete Request - notino: " + notino);
        System.out.println("Current User: " + principal.getName());

        int result = noticeService.deleteNotice(notino);

        System.out.println("Delete Result: " + result);

        if (result > 0) {
            rattr.addFlashAttribute("successMessage", "공지사항이 삭제되었습니다.");
            return "redirect:/inquiry/notice/noti_list";
        } else {
            rattr.addFlashAttribute("errorMessage", "삭제 실패");
            return "redirect:/inquiry/notice/detail?num=" + notino;
        }
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
    public String noticeAdd(Question question, @RequestParam(required = false) String alert) throws Exception {
        // checkbox 값을 Boolean으로 변환
        question.setQalert(Boolean.parseBoolean(alert));

        MultipartFile uploadfile = question.getUploadfile();
        if (!uploadfile.isEmpty()) {
            String fileDBName = questionService.saveUploadFile(uploadfile, "/src/main/resources/static/upload");
            question.setQfile(fileDBName);
        }

        // 문의 저장
        questionService.insertQuestion(question);

        // 알림 요청이 true일 경우 이메일 발송
        if (Boolean.TRUE.equals(question.getQalert()) && question.getResponseemail() != null) {
            EmailMessage emailMessage = EmailMessage.builder()
                    .to(question.getResponseemail())
                    .subject("답변 알림")
                    .message("귀하의 문의에 대한 답변이 등록되었습니다.")
                    .build();

            // 이메일 비동기 발송
            emailService.asyncSendMail(emailMessage);
        }
        return "redirect:/inquiry/question";
    }

    //문의사항 댓글
    @PostMapping(value = "/qlist")
    @ResponseBody
    public Map<String, Object> CommentList(int qno, int page) {
        List<Question> qlist = questionService.getCommentList(qno, page);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("qlist", qlist);

        return map;
    }

    @PostMapping(value = "/add")
    @ResponseBody
    public int commentAdd(Question c) {
        return questionService.commentsInsert(c);
    }

    @PostMapping(value = "/update")
    @ResponseBody
    public int commentUpdate(Question co) {
        return questionService.commentsUpdate(co);
    }

    @PostMapping(value = "/inquiry/comment/delete")
    @ResponseBody
    public int commentDelete(int num) {
        return questionService.commentDelete(num);
    }

}


