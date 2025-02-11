package hello.fclover.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SearchKeywordDTO {

    private final String keyword;

    private final String language;
}
