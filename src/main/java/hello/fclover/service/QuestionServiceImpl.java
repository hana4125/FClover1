package hello.fclover.service;



import hello.fclover.domain.Question;
import hello.fclover.mybatis.mapper.QuestionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionMapper dao;


    @Override
    public int ListCount() {
        return dao.ListCount();
    }

    @Override
    public List<Question> BoardList(Integer page, int limit) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();

        int startrow=(page-1)*limit;
        map.put("start", startrow);
        map.put("limit", limit);

        return dao.BoardList(map);
    }

    @Override
    public void insertQuestion(Question question) {
        dao.insertQuestion(question);
    }

    @Override
    public Question Detail(int no) {
        return dao.Detail(no);
    }


}
