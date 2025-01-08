package hello.fclover.service;

import hello.fclover.domain.AddressBook;
import hello.fclover.domain.Member;
import hello.fclover.domain.Notice;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface NoticeService {

    public int getListCount();

    List<Member> getBoardList(int page, int limit);

    void insertNotice(Notice notice);
}
