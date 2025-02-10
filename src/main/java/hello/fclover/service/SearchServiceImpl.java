package hello.fclover.service;

import hello.fclover.domain.Category;
import hello.fclover.domain.Goods;
import hello.fclover.domain.GoodsImage;
import hello.fclover.domain.Member;
import hello.fclover.dto.SearchDetailParamDTO;
import hello.fclover.dto.SearchKeywordDTO;
import hello.fclover.dto.SearchLogDTO;
import hello.fclover.dto.SearchParamDTO;
import hello.fclover.dto.SearchResponseDTO;
import hello.fclover.mybatis.mapper.CategoryMapper;
import hello.fclover.mybatis.mapper.GoodsMapper;
import hello.fclover.mybatis.mapper.SearchLogMapper;
import hello.fclover.util.KeywordPreprocessor;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    // TODO : 시간 측정 로그 AOP 로 적용하도록 수정하기
    private final GoodsMapper goodsMapper;
    private final GoodsService goodsService;
    private final CountService countService;
    private final CategoryMapper categoryMapper;
    private final SearchLogMapper searchLogMapper;

    @Qualifier("asyncExecutor")
    private final Executor asyncExecutor;

    @Override
    public SearchResponseDTO searchByKeyword(String keyword, String sessionId, Member member) {

        // 검색 로그 기록
        insertSearchLog(keyword, sessionId, member);

        // 1. 검색어 전처리 : 키워드와 언어 프로퍼티
        SearchKeywordDTO preprocessed = KeywordPreprocessor.preprocess(keyword);
        String processedKeyword = preprocessed.getKeyword();
        String language = preprocessed.getLanguage();

        // 로그로 확인
        log.info("전처리 결과 - keyword: {}, language: {}", processedKeyword, language);

        SearchResponseDTO result = new SearchResponseDTO();

        String sort = "latest";
        int page = 1;
        int size = 20;
        int offset = 0;

        Map<String, Object> params = new HashMap<>();
        params.put("keyword", processedKeyword);
        params.put("language", language);
        params.put("sort", sort);
        params.put("offset", offset);
        params.put("size", size);

        // 전체 소요 시간 측정을 위한 시작 시간 기록
        long overallStartTime = System.currentTimeMillis();

        // 2. countByKeyword 비동기 작업
        CompletableFuture<Integer> countFuture = CompletableFuture.supplyAsync(() -> {
            long countStart = System.currentTimeMillis();
            int countResult = countService.countByKeyword(preprocessed);
            long countTime = System.currentTimeMillis() - countStart;
            log.info("[{}] countByKeyword 실행 시간: {} ms", Thread.currentThread().getName(), countTime);
            return countResult;
        }, asyncExecutor);

        // 3. 상품 검색 및 대표 이미지 가져오기 (비동기로 실행)
        CompletableFuture<List<Goods>> searchFuture = CompletableFuture
                .supplyAsync(() -> {
                    long searchStart = System.currentTimeMillis();
                    List<Goods> goodsResult = goodsMapper.findGoodsByKeyword(params);
                    long searchTime = System.currentTimeMillis() - searchStart;
                    log.info("[{}] findGoodsByKeyword 실행 시간: {} ms", Thread.currentThread().getName(), searchTime);
                    return goodsResult;
                }, asyncExecutor)
                .thenApply(searchResults -> {
                    long imageStart = System.currentTimeMillis();
                    for (Goods goods : searchResults) {
                        GoodsImage mainImage = goodsService.getMainImageByGoodsNo(goods.getGoodsNo());
                        goods.setMainImage(mainImage);
                    }
                    long imageTime = System.currentTimeMillis() - imageStart;
                    log.info("[{}] 대표 이미지 가져오기 실행 시간: {} ms", Thread.currentThread().getName(), imageTime);
                    return searchResults;
                });

        // 4. 카테고리별 갯수 세는 로직을 비동기로 처리 (캐시 적용된 메서드 호출)
        CompletableFuture<Map<Category, Integer>> categoryFuture = CompletableFuture.supplyAsync(() -> countService.getCategoryCount(preprocessed), asyncExecutor);

        // 모든 비동기 작업이 완료될 때까지 대기
        int totalCount = countFuture.join();
        List<Goods> searchResults = searchFuture.join();
        Map<Category, Integer> sortedCategoryMap = categoryFuture.join();

        long overallTime = System.currentTimeMillis() - overallStartTime;
        log.info("전체 검색 소요 시간: {} ms", overallTime);

        // goodsPrice 최댓값과 최솟값 계산
        OptionalInt maxPriceOpt = searchResults.stream()
                .mapToInt(Goods::getGoodsPrice)
                .max();
        OptionalInt minPriceOpt = searchResults.stream()
                .mapToInt(Goods::getGoodsPrice)
                .min();

        if (maxPriceOpt.isPresent()) {
            result.setMaxPrice(maxPriceOpt.getAsInt());
            result.setMinPrice(minPriceOpt.getAsInt());
        }

        Map<String, Integer> pagination = calculatePagination(totalCount, page, size);

        result.setSearchResults(searchResults);
        result.setKeyword(keyword);
        result.setTotalCount(totalCount);
        result.setCategoryList(sortedCategoryMap);
        result.setSort(sort);
        result.setCurrentPage(page);
        result.setTotalPages(pagination.get("totalPages"));
        result.setStartPage(pagination.get("startPage"));
        result.setEndPage(pagination.get("endPage"));
        result.setSize(size);

        return result;
    }

    // 상세 검색
    @Override
    public SearchResponseDTO searchDetail(SearchDetailParamDTO searchDetailParamDTO) {

        SearchResponseDTO result = new SearchResponseDTO();

        String sort = "latest";
        int page = 1;
        int size = 20;
        int offset = 0;

        Map<String, Object> params = new HashMap<>();

        params.put("name", searchDetailParamDTO.getCname());
        params.put("writer", searchDetailParamDTO.getChrcDetail());
        params.put("companyName", searchDetailParamDTO.getPbcmDetail());

        params.put("cate", searchDetailParamDTO.getCate());

        params.put("minPrice", searchDetailParamDTO.getSaprmin());
        params.put("maxPrice", searchDetailParamDTO.getSaprmax());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate startDate = LocalDate.parse(searchDetailParamDTO.getRlseStr(), formatter);
        LocalDate endDate = LocalDate.parse(searchDetailParamDTO.getRlseEnd(), formatter);
        params.put("startDate", startDate);
        params.put("endDate", endDate);

        params.put("sort", sort);
        params.put("offset", offset);
        params.put("size", size);

        long countStartTime = System.currentTimeMillis();

        int totalCount = goodsMapper.countGoodsByParam(params);

        long countEndTime = System.currentTimeMillis();
        long totalCountTime = countEndTime - countStartTime;
        log.info("상세 검색어: '{}' 상세 count 완료 소요 시간: {} ms", searchDetailParamDTO.getRepKeyword(), totalCountTime);

        long searchStartTime = System.currentTimeMillis();

        List<Goods> searchResults = goodsMapper.findGoodsByParam(params);

        long searchEndTime = System.currentTimeMillis();
        long totalSearchTime = searchEndTime - searchStartTime;
        log.info("상세 검색어: '{}' 상세 검색 소요 시간 : {} ms", searchDetailParamDTO.getRepKeyword(), totalSearchTime);

        long totalTime = totalCountTime + totalSearchTime;
        log.info("상세 검색어: '{}' 상세 검색에 들어간 총 소요 시간: {} ms", searchDetailParamDTO.getRepKeyword(), totalTime);

        Map<String, Integer> pagination = calculatePagination(totalCount, page, size);

        result.setSearchResults(searchResults);
        result.setKeyword(searchDetailParamDTO.getRepKeyword());
        result.setSort(sort);
        result.setCurrentPage(page);
        result.setTotalPages(pagination.get("totalPages"));
        result.setSize(size);
        result.setStartPage(pagination.get("startPage"));
        result.setEndPage(pagination.get("endPage"));
        result.setTotalCount(totalCount);

        return result;
    }

    // 검색 결과 필터 및 정렬
    @Override
    public SearchResponseDTO refineResult(SearchParamDTO searchParamDTO) {

        int page = searchParamDTO.getPage();

        searchParamDTO.setOffset((page - 1) * searchParamDTO.getSize());
        List<Goods> goodsList = goodsMapper.searchByParam(searchParamDTO);

        for (Goods goods : goodsList) {
            GoodsImage mainImage = goodsService.getMainImageByGoodsNo(goods.getGoodsNo());
            goods.setMainImage(mainImage);
        }

        int size = searchParamDTO.getSize();
        int totalCount = goodsMapper.countSearchByParam(searchParamDTO);
        Map<String, Integer> pagination = calculatePagination(totalCount, page, size);

        SearchResponseDTO result = new SearchResponseDTO();

        // TODO : 카테고리별 갯수 세는 로직 -> 비동기로 수정해야 함
        Map<Category, Integer> categoryList = new HashMap<>();
        for(Goods goods : goodsList) {
            int cateNo = goods.getCateNo();
            Category category = categoryMapper.findTitle(cateNo);
            categoryList.merge(category, 1, Integer::sum);
        }

        // 재검색 키워드가 존재할 경우 검색 결과를 2차 필터링 하는 로직
        if (searchParamDTO.getReKeyword() != null) {
            String reKeyword = searchParamDTO.getReKeyword();
            goodsList = goodsList.stream()
                    .filter(goods ->
                            (goods.getGoodsName() != null && goods.getGoodsName().toLowerCase().contains(reKeyword)) ||
                                    (goods.getGoodsContent() != null && goods.getGoodsContent().toLowerCase().contains(reKeyword)) ||
                                    (goods.getGoodsWriter() != null && goods.getGoodsWriter().toLowerCase().contains(reKeyword)) ||
                                    (goods.getCompanyName() != null && goods.getCompanyName().toLowerCase().contains(reKeyword))
                    )
                    .collect(Collectors.toList());
        }

        // 해당 검색 결과의 최대 가격, 최소 가격 구하는 로직 만들기

        result.setSearchResults(goodsList);
        result.setTotalCount(totalCount);
        result.setCurrentPage(page);
        result.setTotalPages(pagination.get("totalPages"));
        result.setStartPage(pagination.get("startPage"));
        result.setEndPage(pagination.get("endPage"));
        result.setSort(searchParamDTO.getSort());
        result.setSize(searchParamDTO.getSize());
        result.setCategoryList(categoryList);

        return result;
    }

    // 검색 로그 삽입 로직
    @Async
    public void insertSearchLog(String keyword, String sessionId, Member member) {

        // 동일 세션에서 같은 검색어를 연속해서 검색시 로그 삽입하지 않음 (로그 오염 방지)
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpSession session = ((ServletRequestAttributes) requestAttributes).getRequest().getSession();
            String lastKeyword = (String) session.getAttribute("lastSearchKeyword");

            if (keyword.equals(lastKeyword)) {
                return;
            } else {
                session.setAttribute("lastSearchKeyword", keyword);
            }
        }

        SearchLogDTO searchLogDTO = new SearchLogDTO();

        searchLogDTO.setSearchKeyword(keyword);

        searchLogDTO.setSessionId(sessionId);

        if (member != null) {
            searchLogDTO.setMemberNo(member.getMemberNo());


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDate birthDate = LocalDate.parse(member.getBirthdate(), formatter);

            LocalDate today = LocalDate.now();
            int age = Period.between(birthDate, today).getYears();

            int decade = (age / 10) * 10;
            if (decade < 10) {
                searchLogDTO.setMemberAgeRange(null);
            } else if (decade > 40) {
                searchLogDTO.setMemberAgeRange("40대");
            } else {
                searchLogDTO.setMemberAgeRange(decade + "대");
            }


            if(member.getGender().equals("male")) {
                searchLogDTO.setMemberGender("M");
            } else if (member.getGender().equals("female")) {
                searchLogDTO.setMemberGender("F");
            } else {
                searchLogDTO.setMemberGender("N");
            }
        }

        searchLogDTO.setSearchDatetime(LocalDateTime.now());

        searchLogMapper.insertSearchLog(searchLogDTO);
    }

    private Map<String, Integer> calculatePagination(int totalCount, int currentPage, int pageSize) {
        int totalPages = (int)Math.ceil((double)totalCount / pageSize);
        int maxPageNumbersToShow = 10;
        int startPage, endPage;

        if (totalPages <= maxPageNumbersToShow) {
            startPage = 1;
            endPage = totalPages;
        } else {
            if (currentPage <= 6) {
                startPage = 1;
                endPage = 10;
            } else if (currentPage + 4 >= totalPages) {
                startPage = totalPages - 9;
                endPage = totalPages;
            } else {
                startPage = currentPage - 5;
                endPage = currentPage + 4;
            }
        }

        Map<String , Integer> pageInfo = new HashMap<>();
        pageInfo.put("totalPages", totalPages);
        pageInfo.put("startPage", startPage);
        pageInfo.put("endPage", endPage);
        return pageInfo;
    }


}
