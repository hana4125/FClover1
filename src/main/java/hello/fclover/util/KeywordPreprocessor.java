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
            // only korean
            language = "ko";
            processedKeyword = keyword.replaceAll("[^가-힣]", "");
        } else if (englishCount > 0 && koreanCount == 0) {
            // only english
            language = "en";
            processedKeyword = keyword.replaceAll("[^A-Za-z]", "");
        } else if (koreanCount > 0 && englishCount > 0) {

            if (koreanCount > englishCount) {
                language = "ko";
                processedKeyword = keyword.replaceAll("[^가-힣]", "");
            } else {
                language = "en";
                processedKeyword = keyword.replaceAll("[^A-Za-z]", "");
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
