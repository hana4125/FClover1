package hello.fclover.common;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CacheType {
    CATEGORY_FINDALL("CategoryMapper.findAll", 10, 10000),
    KEYWORD_COUNT_CACHE("keywordCountCache", 10, 1000),
    CATEGORY_COUNT_CACHE("categoryCountCache", 10, 1000);

    private final String cacheName;
    private final int expiredAfterWrite;
    private final int maximumSize;
}

