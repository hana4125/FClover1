package hello.fclover.service;



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

import java.io.File;
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

    @Value("${file.upload.directory}")
    private String uploadDirectory;

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

        if (startDate == null || endDate == null) {
            return Collections.emptyList(); // 날짜가 없으면 빈 리스트 반환
        }

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
    public String saveUploadFile(MultipartFile uploadfile) throws Exception {
        if (uploadfile.isEmpty()) {
            return null;
        }

        File directory = new File(uploadDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String originalFilename = uploadfile.getOriginalFilename(); //원본 파일명 가져오기
        String fileExtension = getFileExtension(originalFilename);
        String fileDBName = generateUniqueFileName(originalFilename);

        // 파일 저장
        Path filePath = Paths.get(uploadDirectory, fileDBName);
        Files.copy(uploadfile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return fileDBName;
    }

    /**  파일명 생성 (날짜 + 랜덤값) */
    private String generateUniqueFileName(String fileExtension) {
        LocalDateTime now = LocalDateTime.now();
        return String.format("file_%s_%s.%s",
                now.format(DateTimeFormatter.BASIC_ISO_DATE),
                UUID.randomUUID().toString().substring(0, 8),
                fileExtension);
    }

    /**  파일 확장자 가져오기 */
    private String getFileExtension(String fileName) {
        return Optional.ofNullable(fileName)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(fileName.lastIndexOf(".") + 1))
                .orElse("");
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
            return result;
        }catch (Exception e){
            log.error("삭제 실패", qno, e.getMessage());
        }
        return 0;
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
