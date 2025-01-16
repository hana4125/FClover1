package hello.fclover.service;



import hello.fclover.domain.Notice;
import hello.fclover.domain.Question;

import java.util.List;

public interface QuestionService {


    int ListCount();

    List<Question> BoardList(Integer page, int limit);

    void insertQuestion(Question question);

    Question Detail(int no);
}
