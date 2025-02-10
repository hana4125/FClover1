package hello.fclover.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchLogDTO {

    // 조회시 사용
    private Long searchLogId;

    // 입력받을 값들
    private String searchKeyword;
    private Long memberNo;
    private String memberAgeRange;
    private String memberGender;
    private String sessionId;

    // 조회시 사용
    private LocalDateTime searchDatetime;
}
