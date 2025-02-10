package hello.fclover.util;

import hello.fclover.dto.SearchKeywordDTO;

public class KeywordPreprocessor {

    public static SearchKeywordDTO preprocess(String keyword) {
        int koreanCount = 0;
        int englishCount = 0;
        for (char c : keyword.toCharArray()) {
            if (isKorean(c)) {
                koreanCount++;
            } else if(isEnglish(c)) {
                englishCount++;
            }
        }

        String language;
        String processedKeyword;

        if (koreanCount > 0 && englishCount == 0) {
            // 한글만 있는 경우
            language = "ko";
            processedKeyword = keyword.replaceAll("[^가-힣0-9]", "");
        } else if (englishCount > 0 && koreanCount == 0) {
            // 영어만 있는 경우
            language = "en";
            processedKeyword = keyword.replaceAll("[^A-Za-z0-9]", "");
        } else if (koreanCount > 0 && englishCount > 0) {
            // 혼합된 경우
            if (koreanCount > englishCount) {
                language = "ko";
                processedKeyword = keyword.replaceAll("[^가-힣0-9]", "");
            } else {
                language = "en";
                processedKeyword = keyword.replaceAll("[^A-Za-z0-9]", "");
            }
        } else {
            language = "unknown";
            processedKeyword = keyword;
        }

        return new SearchKeywordDTO(processedKeyword, language);
    }

    private static boolean isKorean(char c) {
        Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
        return (block == Character.UnicodeBlock.HANGUL_SYLLABLES ||
                block == Character.UnicodeBlock.HANGUL_JAMO ||
                block == Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO);
    }

    private static boolean isEnglish(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }
}
