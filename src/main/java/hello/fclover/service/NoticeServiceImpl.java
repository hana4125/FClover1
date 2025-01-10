package hello.fclover.service;

import hello.fclover.domain.Member;
import hello.fclover.domain.Notice;
import hello.fclover.mybatis.mapper.NoticeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeMapper dao;

    @Override
    public int getListCount() {
        return dao.getListCount();
    }

    @Override
    public List<Notice> getBoardList(int page, int limit) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        int startrow=(page-1)*limit;

        map.put("start", startrow);
        map.put("limit", limit);

        return dao.getBoardList(map);
    }

    @Override
    public void insertNotice(Notice notice) {
        dao.insertNotice(notice);
    }

    @Override
    public void setReadCountUpdate(int num) {

    }

    @Override
    public Notice getDetail(int num) {
        return dao.getDetail(num);
    }

    @Override
    public int getSearchListCount( String search_word) {
        Map<String, Object> map = new HashMap<String, Object>();
            map.put("search_word",  "%"+ search_word + "%");

        return dao.getSearchListCount(map);
    }

    @Override
    public List<Notice> getSearchList( String search_word, int page, int limit) {

        Map<String, Object> map = new HashMap<String, Object>();
            map.put("search_word",  "%"+ search_word + "%");
            map.put("start", (page - 1) * limit);
            map.put("size", limit);

        return dao.getSearchList(map);
    }
}
