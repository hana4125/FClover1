package hello.fclover.service;

import hello.fclover.domain.AddressBook;
import hello.fclover.domain.Member;
import hello.fclover.domain.Notice;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface NoticeService {

    public int getListCount();

    List<Notice> getBoardList(int page, int limit);

    void insertNotice(Notice notice);

    void setReadCountUpdate(int num);

    Notice getDetail(int num);

    int getSearchListCount( String searchWord);

    List<Notice> getSearchList( String searchWord, int page, int limit);
}
