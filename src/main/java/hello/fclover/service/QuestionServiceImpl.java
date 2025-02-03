package hello.fclover.service;



import hello.fclover.domain.Question;
import hello.fclover.mail.EmailMessage;
import hello.fclover.mail.EmailService;
import hello.fclover.mybatis.mapper.NoticeMapper;
import hello.fclover.mybatis.mapper.QuestionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionMapper dao;
    private final EmailService emailService;
    private final NoticeMapper noticeMapper;



    @Override
    public int TotalCount() {
        return dao.TotalCount();
    }

    @Override
    public List<Question> BoardList(Integer currentPage, int limit) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();

        int startrow=(currentPage - 1)*limit;
        map.put("start", startrow);
        map.put("limit", limit);

        return dao.BoardList(map);
    }

    @Override
    public List<Question> getFilteredQuestions(LocalDate startDate, LocalDate endDate, int page, int limit) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("start", (page - 1) * limit); // 페이지네이션 시작 위치
        map.put("limit", limit); // 페이지당 항목 수

        return dao.getFilteredQuestions(map);
    }

    @Override
    public int getFilteredCount(LocalDate startDate, LocalDate endDate) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("startDate", startDate);
        map.put("endDate", endDate);

        return dao.getFilteredCount(map);
    }


    @Override
    public void insertQuestion(Question question) {
        log.info("Before insert - qno: {}", question.getQno());
        dao.insertQuestion(question);
        log.info("After insert - qno: {}", question.getQno());
    }

    @Override
    public Question Detail(int no) {
        return dao.Detail(no);
    }

    //댓글 서비스
    @Override
    public List<Question> getCommentList(int q_no, int page) {
        int startrow = 1;
        int endrow = page * 3;

        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("q_no", q_no);
        map.put("start", startrow -1);
        map.put("end", endrow);
        return dao.getCommentList(map);
    }

    //댓글 작성
    @Override
    public int commentsAdd(String ccontent, String memberid, int qno) {
        return dao.commentsAdd(ccontent, memberid, qno);
    }

    @Override
    public int commentDelete(int cno) {
        return dao.commentDelete(cno);
    }


    @Override
    public int commentsUpdate(Question co) {
        return dao.commentsUpdate(co);
    }

    @Override
    public String getQvalue(String qtype) {
        return dao.getQvalue(qtype);
    }

    @Override
    public void saveInquiry(String phone, String email, String message, boolean alert) {
        // 문의 정보 저장
        Question qs = new Question();
        qs.setResponsephone(phone);
        qs.setResponseemail(email);
        qs.setQalert(alert);

        // 문의 저장
        dao.insertQuestionSave(qs);

        // 알림 요청이 있을 경우 이메일 발송
        if (alert) {
            EmailMessage emailMessage = EmailMessage.builder()
                    .to(email)
                    .subject("답변 알림")
                    .message("귀하의 문의에 대한 답변이 등록되었습니다.")
                    .build();

            // 이메일 비동기 발송
            emailService.asyncSendMail(emailMessage);  // 비동기 이메일 발송
        }
    }

    @Override
    public Question getQuestionDetail(int qno) {
        return dao.findByQno(qno);
    }

}
