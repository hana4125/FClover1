package hello.fclover.service;

import hello.fclover.domain.Member;
import hello.fclover.domain.Notice;
import hello.fclover.mybatis.mapper.NoticeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;


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
    public List<Member> getBoardList(int page, int limit) {
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
}
