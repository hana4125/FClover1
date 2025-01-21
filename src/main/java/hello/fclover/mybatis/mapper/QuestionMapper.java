package hello.fclover.mybatis.mapper;


import hello.fclover.domain.Question;
import org.apache.ibatis.annotations.Mapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Mapper
public interface QuestionMapper {

    int TotalCount();

    List<Question> BoardList(HashMap<String, Integer> map);

    void insertQuestion(Question question);

    Question Detail(int no);

    //댓글
    List<Question> getCommentList(Map<String, Integer> map);

    int commentsInsert(Question c);

    int commentDelete(int num);

    int commentsUpdate(Question co);
}
