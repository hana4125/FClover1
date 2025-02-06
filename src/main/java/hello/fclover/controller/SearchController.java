package hello.fclover.controller;

import hello.fclover.domain.Category;
import hello.fclover.domain.Member;
import hello.fclover.dto.PopularKeywordResponseDTO;
import hello.fclover.dto.SearchDetailParamDTO;
import hello.fclover.dto.SearchParamDTO;
import hello.fclover.dto.SearchResponseDTO;
import hello.fclover.service.CategoryService;
import hello.fclover.service.MemberService;
import hello.fclover.service.SearchService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequestMapping(value = "/search")
@RequiredArgsConstructor
public class SearchController {

    // 의존성 주입
    private final SearchService searchService;
    private final CategoryService categoryService;
    private final MemberService memberService;
    private final RedisTemplate<String, String> redisTemplate;

    // 멤버 로그인 정보
    @ModelAttribute("member")
    public Member addMemberToModel(Principal principal) {

        if (principal != null) {
            String memberId = principal.getName();
            return memberService.findMemberById(memberId);
        }

        return null;
    }


    // 키워드 검색
    @GetMapping("/searchKeyword")
    public String keywordSearch(Model model,
            @RequestParam("keyword") String keyword,
            HttpServletRequest request,
            @ModelAttribute("member") Member member) {

        // 세션 아이디 가져오기
        HttpSession session = request.getSession();
        String sessionId = session.getId();

        SearchResponseDTO result = searchService.searchByKeyword(keyword, sessionId, member);

        model.addAttribute("searchResult", result);

        return "user/userSearchResult";
    }


    // 상세 검색 페이지
    @GetMapping("/searchDetail")
    public String detailSearch(Model model) {

        List<Category> categoryList = categoryService.getCategoryList();
        model.addAttribute("categoryList", categoryList);

        return "user/userSearchDetail";
    }


    // 상세 검색 결과
    @GetMapping("/searchDetailResult")
    public String searchDetailResult(Model model, SearchDetailParamDTO param) {

        SearchResponseDTO result = searchService.searchDetail(param);
        model.addAttribute("searchResult", result);

        return "user/userSearchResult";
    }


    // 검색 필터링 Ajax
    @GetMapping("/refineAjax")
    public ResponseEntity<SearchResponseDTO> refineResultsAjax(@ModelAttribute SearchParamDTO param) {

        // 서비스 호출
        SearchResponseDTO result = searchService.refineResult(param);

        // JSON 으로 응답
        return ResponseEntity.ok(result);
    }

    // 인기 검색어 조회 Ajax
    @GetMapping("/api/popular-keywords")
    @ResponseBody
    public PopularKeywordResponseDTO getPopularKeywords(
            @RequestParam(value = "gender", defaultValue = "ALL") String gender,
            @RequestParam(value = "ageRange", defaultValue = "ALL") String ageRange,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {

        // Redis Key 구성
        String redisKey = "popular:" + gender + ":" + ageRange;

        // Redis Sorted Set 에서 점수가 높은 순으로 상위 limit 개 항목 조회
        Set<ZSetOperations.TypedTuple<String>> resultSet = redisTemplate.opsForZSet()
                        .reverseRangeWithScores(redisKey, 0, limit - 1);

        // 조회 결과를 Keyword 리스트로 변환
        List<String> keywords = (resultSet != null)
                ? resultSet.stream()
                .map(ZSetOperations.TypedTuple::getValue)
                .collect(Collectors.toList())
                : new ArrayList<>();

        // 기준 시각 (예: 현재 시각)을 "MM.DD.HH:mm" 형식으로 포맷
        // (day-of-month는 소문자 dd를 사용합니다. 예: "03.15.14:30")
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM.dd.HH:mm");
        LocalDateTime now = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);
        String baseTime = now.format(formatter);

        return new PopularKeywordResponseDTO(keywords, baseTime);
    }


}