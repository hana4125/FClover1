package hello.fclover.service;



import hello.fclover.domain.Notice;
import hello.fclover.domain.Question;

import java.util.List;

public interface QuestionService {

    int TotalCount();

    List<Question> BoardList(Integer currentPage, int limit);

    void insertQuestion(Question question);

    Question Detail(int no);



    //댓글 목록 가져오기
    public List<Question> getCommentList(int board_num, int page);

    //댓글 등록하기
    public int commentsInsert(Question c);

    //댓글 삭제
    public int commentDelete(int num);

    //댓글 수정
    public int commentsUpdate(Question co);

}
