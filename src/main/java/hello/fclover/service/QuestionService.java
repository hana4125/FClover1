package hello.fclover.service;



import hello.fclover.domain.Notice;
import hello.fclover.domain.Question;

import java.util.List;

public interface QuestionService {

    int TotalCount();

    List<Question> BoardList(Integer currentPage, int limit);

    void insertQuestion(Question question);

    Question Detail(int no);


}
