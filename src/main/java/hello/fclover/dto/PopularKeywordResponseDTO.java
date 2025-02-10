package hello.fclover.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PopularKeywordResponseDTO {
    private List<String> keywords;
    private String baseTime;
}
