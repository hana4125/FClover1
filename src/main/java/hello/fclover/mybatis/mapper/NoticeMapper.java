package hello.fclover.mybatis.mapper;

import hello.fclover.domain.Notice;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface NoticeMapper {

    int getListCount();

    List<Notice> getBoardList(HashMap<String, Integer> map);

    void insertNotice(Notice notice);

    Notice getDetail(int num);

    int getSearchListCount(Map<String, Object> map);

    List<Notice> getSearchList(Map<String, Object> map);
}
