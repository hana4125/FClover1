package hello.fclover.domain;

import java.time.LocalDateTime;

public class comments extends Question {
    private Long commentId;
    private Long qno;          // 문의글 번호
    private String content;
    private String writer;     // 작성자 (admin/seller)
    private LocalDateTime createDate;
}
