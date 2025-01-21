package hello.fclover.service;



import hello.fclover.domain.Question;
import hello.fclover.mybatis.mapper.QuestionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionMapper dao;


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
    public void insertQuestion(Question question) {
        dao.insertQuestion(question);
    }

    @Override
    public Question Detail(int no) {
        return dao.Detail(no);
    }


    //댓글 서비스
    @Override
    public List<Question> getCommentList(int board_num, int page) {
        int startrow = 1;
        int endrow = page * 3;

        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("board_num", board_num);
        map.put("start", startrow);
        map.put("end", endrow);
        return dao.getCommentList(map);
    }

    @Override
    public int commentsInsert(Question c) {
        return dao.commentsInsert(c);
    }

    @Override
    public int commentDelete(int num) {
        return dao.commentDelete(num);
    }


    @Override
    public int commentsUpdate(Question co) {
        return dao.commentsUpdate(co);
    }


}
