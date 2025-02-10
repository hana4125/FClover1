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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.xml.stream.events.Comment;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
    @PostMapping(value = "/notice/add")
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
            mv.addObject("url", request.getRequestURL());
            mv.addObject("message", "상세보기 실패입니다.");
        } else {
            mv.setViewName("user/userNoticeDetail");
            mv.addObject("notidata", notice);
        }
        return mv;
    }

    //공지사항 검색
    @GetMapping(value = "/notice/noti_list")
    public ModelAndView noticeList(
            Principal principal,
            ModelAndView mv,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "") String search_word,
            @RequestParam(defaultValue = "전체") String category) {
        // 검색어가 없을 경우 처리
        String searchQuery = search_word.trim().isEmpty() ? null : search_word;
        int listcount = noticeService.getSearchListCount(searchQuery, category);
        List<Notice> list = noticeService.getSearchList(searchQuery, page, limit, category);
        PaginationResult result = new PaginationResult(page, limit, listcount);

        System.out.println("listcount = " + listcount);
        System.out.println("noticelist = " + list);

        mv.setViewName("user/userNotice");
        mv.addObject("page", page);
        mv.addObject("maxpage", result.getMaxpage());
        mv.addObject("startpage", result.getStartpage());
        mv.addObject("endpage", result.getEndpage());
        mv.addObject("noticelist", list);
        mv.addObject("search_word", search_word);
        mv.addObject("category", category);
        mv.addObject("limit", limit);
        mv.addObject("listcount", listcount);
        return mv;
    }

    //공지사항 수정
    @GetMapping("/notice/modifyView")
    public String modifyView(@RequestParam("num") int notino, Model model) {
        Notice notice = noticeService.getNoticeById(notino);
        model.addAttribute("notidata", notice);
        return "user/userNoticeModify";
    }

    @PostMapping("/notice/modify")
    public String updateNotice(@ModelAttribute Notice notice) {
        noticeService.modifyNotice(notice);
        return "redirect:/inquiry/notice/detail?num=" + notice.getNotino();
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
            return "redirect:/inquiry/notice/detail?notino=" + notino;
        }
        int result = noticeService.deleteNotice(notino);
        if (result > 0) {
            rattr.addFlashAttribute("successMessage", "공지사항이 삭제되었습니다.");

            return "redirect:/inquiry/notice/noti_list";
        } else {
            rattr.addFlashAttribute("errorMessage", "삭제 실패");
            return "redirect:/inquiry/notice/detail?notino=" + notino;
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

    //문의 기간 조회
    @GetMapping("/question/filter")
    public String filterQuestions(
            @RequestParam(defaultValue = "1") Integer currentPage,

            @RequestParam(defaultValue = "") String startDate,
            @RequestParam(defaultValue = "") String endDate,
            Model m) {
        LocalDate start = null;
        LocalDate end = null;

        if (!(startDate.equals("") && endDate.equals(""))) {
            start = LocalDate.parse(startDate);
            end = LocalDate.parse(endDate);
        }
        int limit = 10;
        int totalcount = questionService.getFilteredCount(start, end);
        List<Question> questionlist = questionService.getFilteredQuestions(start, end, currentPage, limit);

        PaginationResult rt = new PaginationResult(currentPage, limit, totalcount);
        m.addAttribute("currentPage", currentPage);
        m.addAttribute("maxpage", rt.getMaxpage());
        m.addAttribute("startpage", rt.getStartpage());
        m.addAttribute("endpage", rt.getEndpage());
        m.addAttribute("totalcount", totalcount);
        m.addAttribute("questionlist", questionlist);
        m.addAttribute("limit", limit);

        m.addAttribute("startDate", startDate);
        m.addAttribute("endDate", endDate);

        return "user/userQNA";
    }

    //문의사항 작성
    @GetMapping(value = "/question/write")
    public String questionWrite() {
        return "user/userQNAWrite";
    }

    //문의사항 파일 등록
    @PostMapping(value = "/question/plus")
    public String noticeAdd(
            Question question,
            @RequestParam(value = "qnaImage", required = false) MultipartFile multipartFile) throws Exception {

        // 요청이 들어왔는지 확인
        System.out.println("noticeAdd 요청 도착");

        if (multipartFile != null) {
            System.out.println("파일 이름: " + multipartFile.getOriginalFilename());
            System.out.println("파일 크기: " + multipartFile.getSize());
        } else {
            System.out.println("qnaImage 파일이 포함되지 않음");
        }

        if (multipartFile != null && !multipartFile.isEmpty()) {
            String fileDBName = questionService.saveFile(multipartFile);
            question.setQfile(fileDBName);
        }
        questionService.insertQuestion(question);
        return "redirect:/inquiry/question/detail?qno=" + question.getQno();
    }

    /*댓글 달리면 이메일 발송*/
    @PostMapping(value = "/commentAdd")
    @ResponseBody
    public int CommentsAdd(@RequestParam String ccontent,
                           @AuthenticationPrincipal UserDetails userDetails,
                           @RequestParam int qno) {
        String memberid = userDetails.getUsername();
        int result = questionService.commentsAdd(ccontent, memberid, qno);

        if (result > 0) {  // 댓글 추가 성공
            Question question = questionService.getQuestionDetail(qno);
            log.info("문의글 정보: {}", question);

            // 알림 요청이 있고 이메일이 있는 경우에만 발송
            if (Boolean.TRUE.equals(question.getQalert()) && question.getResponseemail() != null) {
                log.info("이메일 발송 시작: {}", question.getResponseemail());

                EmailMessage emailMessage = EmailMessage.builder()
                        .to(question.getResponseemail())
                        .subject("[네잎클로버] 문의하신 글에 답변이 등록되었습니다.")
                        .message(createEmailContent(question, ccontent))
                        .build();
                emailService.asyncSendMail(emailMessage);
            } else {
                log.info("이메일 발송 조건 불충족: qalert={}, responseemail={}",
                        question.getQalert(), question.getResponseemail());
            }
        }
        return result;
    }

    private String createEmailContent(Question question, String ccontent) {
        return String.format("""
                        안녕하세요, 네잎클로버입니다.
                        
                        문의하신 글에 답변이 등록되었습니다.
                        
                        %s
                        
                        %s
                        
                        자세한 내용은 아래 링크에서 확인하실 수 있습니다.
                        http://localhost:8080/inquiry/question/detail?qno=%d
                        
                        감사합니다.
                        네잎클로버 드림
                        """,
                question.getQtitle(),
                ccontent,
                question.getQno()
        );
    }

    @GetMapping("/question/detail")
    public ModelAndView questionDetail(@RequestParam(value = "qno", required = false) Integer qno,
                                       ModelAndView mv) {

        // qno가 null이면 문의 목록 페이지로 리다이렉트
        if (qno == null) {
            mv.setViewName("redirect:/inquiry/question");
            return mv;
        }

        // 질문을 qno(질문번호)로 조회
        Question question = questionService.getQuestionDetail(qno);

        // 질문이 없으면 에러 페이지로
        if (question == null) {
            mv.setViewName("error/error");
            mv.addObject("message", "Question not found.");
        } else {
            mv.setViewName("user/userQnaDetail");
            mv.addObject("questiondata", question);
        }
        return mv;
    }


    //문의사항 삭제
    @PostMapping(value = "/question/delete")
    public String deleteQuestion(@RequestBody Map<String, Integer> body,
                                 RedirectAttributes rattr,
                                 Principal principal) {
        int qno = body.get("num");

        // 관리자 권한 체크
        if (!principal.getName().equals("admin")) {
            rattr.addFlashAttribute("errorMessage", "삭제 권한이 없습니다.");
            return "redirect:/inquiry/question/detail?qno=" + qno;
        }
        int result = questionService.deleteQuestion(qno);
        log.debug("삭제 결과 - qno: {}, result: {}", qno, result);
        if (result > 0) {
            rattr.addFlashAttribute("successMessage", "문의사항이 삭제되었습니다.");
            return "redirect:/inquiry/question";
        } else {
            rattr.addFlashAttribute("errorMessage", "삭제 실패");
            return "redirect:/inquiry/question/detail?qno=" + qno;
        }
    }


    //문의사항 댓글
    @PostMapping("/qlist")
    @ResponseBody
    public Map<String, Object> getCommentList(
            @RequestParam int qno,
            @RequestParam int page,
            Authentication authentication
    ) {
        Map<String, Object> response = new HashMap<>();

        // 기존 댓글 목록 로직
        List<Question> qlist = questionService.getCommentList(qno, page);

        // 관리자 권한 확인
        boolean isAdmin = authentication != null &&
                authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        response.put("qlist", qlist);
        response.put("isAdmin", isAdmin);

        return response;
    }

    //문의사항 댓글 수정
    @PostMapping(value = "/update")
    @ResponseBody
    public ResponseEntity<Integer> commentUpdate(@RequestParam("cno") int cno,
                                                 @RequestParam("ccontent") String ccontent) {
        try {
            System.out.println("수정 요청: cno=" + cno + ", 내용=" + ccontent); // 디버깅
            Question co = new Question();
            co.setCno(cno);
            co.setCcontent(ccontent);
            int result = questionService.commentsUpdate(co);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0);
        }
    }

    //문의사항 댓글 삭제
    @PostMapping(value = "/delete")
    @ResponseBody
    public ResponseEntity<Integer> commentDelete(
            @RequestParam("cno") int cno,
            Authentication authentication
    ) {
        // 현재 로그인한 사용자의 권한 확인
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(0);
        }

        try {
            int result = questionService.commentDelete(cno);

            if (result > 0) {
                return ResponseEntity.ok(1);
            } else {
                return ResponseEntity.ok(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0);
        }
    }
}




