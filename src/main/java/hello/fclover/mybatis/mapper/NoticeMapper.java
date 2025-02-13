package hello.fclover.mybatis.mapper;

import hello.fclover.domain.AddressBook;
import hello.fclover.domain.Member;
import hello.fclover.domain.Notice;
import hello.fclover.domain.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mapper
public interface NoticeMapper {

    int getListCount();

    List<Notice> getBoardList(HashMap<String, Integer> map);

    void insertNotice(Notice notice);

    Notice getDetail(int num);

    int getSearchListCount(Map<String, Object> map);

    List<Notice> getSearchList(Map<String, Object> map);

    int deleteNotice(@Param("notino")int notino);

    void deleteAll();

    void save(Notice notice);

    Optional<Notice> findById(int notino);

    void modifyNotice(Notice notice);
}
