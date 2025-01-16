package hello.fclover.mybatis.mapper;


import hello.fclover.domain.Question;
import org.apache.ibatis.annotations.Mapper;
import java.util.HashMap;
import java.util.List;


@Mapper
public interface QuestionMapper {

    int ListCount();

    List<Question> BoardList(HashMap<String, Integer> map);

    void insertQuestion(Question question);

    Question Detail(int no);
}
