package hello.fclover.mybatis.mapper;


import hello.fclover.domain.Question;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Mapper
public interface QuestionMapper {

    int TotalCount();
    List<Question> BoardList(HashMap<String, Integer> map);
    void insertQuestion(Question question);
    Question Detail(int num);

    String getQvalue(String qtype);
    Question findByQno(int qno);

    //댓글
    List<Question> getCommentList(Map<String, Integer> map);
    int commentsAdd(String ccontent, String memberid, int qno);
    int commentsUpdate(Question co);
    int commentDelete(int cno);

    void insertQuestionSave(Question qs);

    //기간조회
    List<Question> getFilteredQuestions(HashMap<String, Object> map);
    List<Question> findByDateBetween(HashMap<String, Object> map);
    int countByDateBetween(LocalDate startDate, LocalDate endDate);
    int countAll();
    List<Question> findAll(HashMap<String, Object> map);

    int deleteQuestion(int qno);

    public int modifyQuestion(Question question);
}
