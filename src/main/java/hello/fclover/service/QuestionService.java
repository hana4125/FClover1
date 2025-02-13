package hello.fclover.service;



import hello.fclover.domain.Notice;
import hello.fclover.domain.Question;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

public interface QuestionService {

    
    List<Question> BoardList(Integer currentPage, int limit);
    
    void insertQuestion(Question question);
    String saveFile(MultipartFile multipartFile) throws Exception;
    Question Detail(int no);

    // 댓글 관련 기능
    List<Question> getCommentList(int board_num, int page);
    int commentsAdd(String ccontent, String memberid, int qno);
    int commentDelete(int cno);
    int commentsUpdate(Question co);

    // 문의 관련 기능
    String getQvalue(String qtype);
    void saveInquiry(String phone, String email, String message, boolean alert);
    Question getQuestionDetail(int qno);


    List<Question> getFilteredQuestions(LocalDate startDate, LocalDate endDate, int page, int limit);
    int getFilteredCount(LocalDate startDate, LocalDate endDate);
    int getTotalCount();
    List<Question> getAllQuestions(Integer currentPage, int limit);
    int TotalCount();

    int deleteQuestion(int qno);

    Question getQuestionById(int qno);

    int modifyQuestion(Question question);
}
