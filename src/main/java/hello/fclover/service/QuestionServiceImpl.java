package hello.fclover.service;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import hello.fclover.domain.Question;
import hello.fclover.mail.EmailMessage;
import hello.fclover.mail.EmailService;
import hello.fclover.mybatis.mapper.NoticeMapper;
import hello.fclover.mybatis.mapper.QuestionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionMapper dao;
    private final EmailService emailService;
    private final NoticeMapper noticeMapper;
    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    //게시글 개수 조회
    @Override
    public int TotalCount() {
        return dao.TotalCount();
    }

    //게시글 목록 조회
    @Override
    public List<Question> BoardList(Integer currentPage, int limit) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        int startrow=(currentPage - 1)*limit;
        map.put("start", startrow);
        map.put("limit", limit);
        return dao.BoardList(map);
    }

    //기간별 게시글 필터링
    @Override
    public List<Question> getFilteredQuestions(LocalDate startDate, LocalDate endDate,
                                               int currentPage, int limit) {

        int offset = (currentPage - 1) * limit;
        HashMap<String, Object> map = new HashMap<>();
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("offset", offset);
        map.put("limit", limit); // 페이지당 항목 수

        return dao.findByDateBetween(map);
    }

    //필터링된 총 개수 조회
    @Override
    public int getFilteredCount(LocalDate startDate, LocalDate endDate) {
        return dao.countByDateBetween(startDate, endDate);
    }

    @Override
    public int getTotalCount() {
        return dao.countAll();
    }

    @Override
    public List<Question> getAllQuestions(Integer currentPage, int limit) {
        int offset = (currentPage - 1) * limit;
        HashMap<String, Object> map = new HashMap<>();
        map.put("offset", offset);
        map.put("limit", limit);
        return dao.findAll(map);
    }

    //게시글 등록
    @Override
    public void insertQuestion(Question question) {
        dao.insertQuestion(question);
    }

    //파일 업로드
    @Override
    public String saveFile(MultipartFile multipartFile) throws Exception {

        String originalFilename = multipartFile.getOriginalFilename();
        String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFilename;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, uniqueFileName, multipartFile.getInputStream(), metadata);
        return amazonS3.getUrl(bucket, uniqueFileName).toString();
    }



    /**  현재 날짜 가져오기 */
    public int[] getCurrentDate() {
        LocalDate now = LocalDate.now();
        return new int[]{now.getYear(), now.getMonthValue(), now.getDayOfMonth()};
    }

    //게시글 상세 조회
    @Override
    public Question Detail(int no) {
        return dao.Detail(no);
    }

    //문의글 삭제
    @Override
    public int deleteQuestion(int qno) {
        try {
            int result = dao.deleteQuestion(qno);
            log.info("deleteQuestion 결과: {}", result);
            return result;
        }catch (Exception e){
            log.error("질문 삭제 중 오류 발생 - qno: {}", qno);
            log.error("예외 종류: {}", e.getClass().getName());
            log.error("예외 메시지: {}", e.getMessage());
            log.error("상세 스택트레이스: ", e);
        }
        return 0;
    }

    //문의 수정
    @Override
    public Question getQuestionById(int qno) {
        return dao.findByQno(qno);
    }

    @Override
    public int modifyQuestion(Question question) {
        return dao.modifyQuestion(question);
    }

    //댓글 목록 가져오기
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

    //타입 값 가져오기
    @Override
    public String getQvalue(String qtype) {
        return dao.getQvalue(qtype);
    }

    //문의하기 저장, 이메일 알림
    @Override
    public void saveInquiry(String phone, String email, String message, boolean alert) {
        Question qs = new Question();
        qs.setResponsephone(phone);
        qs.setResponseemail(email);
        qs.setQalert(alert);

        dao.insertQuestionSave(qs);

        // 알림 요청이 있을 경우 이메일 발송
        if (alert) {
            EmailMessage emailMessage = EmailMessage.builder()
                    .to(email)
                    .subject("답변 알림")
                    .message("귀하의 문의에 대한 답변이 등록되었습니다.")
                    .build();

            // 이메일 비동기 발송
            emailService.asyncSendMail(emailMessage);
        }
    }

    // 게시글 상세 조회
    @Override
    public Question getQuestionDetail(int qno) {
        return dao.findByQno(qno);
    }
}
