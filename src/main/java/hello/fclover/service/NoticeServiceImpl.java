package hello.fclover.service;

import com.github.javafaker.Faker;
import hello.fclover.domain.Notice;
import hello.fclover.mybatis.mapper.NoticeMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.keyvalue.core.KeyValueOperations;
import org.springframework.data.keyvalue.repository.support.SimpleKeyValueRepository;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.time.LocalDate.now;


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
//        log.info("Insert Notice: {}", notice);
        if (notice.getNotititle() == null || notice.getNotiname() == null) {
            throw new IllegalArgumentException("필수 필드 누락");
        }
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
    public int getSearchListCount(String search_word, String category) {
        Map<String, Object> map = new HashMap<>();

        // 검색어 처리
        if (search_word == null || search_word.trim().isEmpty()) {
            map.put("search_word", null);
        } else {
            map.put("search_word", search_word);
        }
        // 카테고리 처리
        if (category == null || category.equals("전체")) {
            map.put("category", null);
        } else {
            map.put("category", category);
        }
        return dao.getSearchListCount(map);
    }

    @Override
    public List<Notice> getSearchList(String search_word, int page, int limit, String category) {
        Map<String, Object> map = new HashMap<>();

        // 검색어 처리
        if (search_word == null || search_word.trim().isEmpty()) {
            map.put("search_word", null);
        } else {
            map.put("search_word", search_word);
        }

        // 카테고리 처리
        if (category == null || category.equals("전체")) {
            map.put("category", null);
        } else {
            map.put("category", category);
        }
        map.put("start", (page - 1) * limit);
        map.put("limit", limit);
        return dao.getSearchList(map);
    }

    @Override
    public int deleteNotice(int notino) {
        try {
            int result = dao.deleteNotice(notino);
            log.error("Delete attempt - notino: {}, rows affected: {}", notino, result);
            return result;
        } catch (Exception e) {
            log.error("Delete failed - notino: {}, error: {}", notino, e.getMessage());
            return 0;
        }
    }

    @Override
    public Notice getNoticeById(int notino) {
        return dao.findById((long) notino)
                .orElseThrow(() -> new RuntimeException("Notice not found"));
    }
    @Override
    public void modifyNotice(Notice notice) {
            dao.modifyNotice(notice);
    }


}
