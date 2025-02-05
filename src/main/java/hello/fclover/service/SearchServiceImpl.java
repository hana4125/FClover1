package hello.fclover.service;

import hello.fclover.domain.Category;
import hello.fclover.domain.Goods;
import hello.fclover.domain.GoodsImage;
import hello.fclover.dto.CategoryCountDTO;
import hello.fclover.dto.SearchDetailParamDTO;
import hello.fclover.dto.SearchParamDTO;
import hello.fclover.dto.SearchResponseDTO;
import hello.fclover.mybatis.mapper.CategoryMapper;
import hello.fclover.mybatis.mapper.GoodsMapper;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    // TODO : 시간 측정 로직 AOP 로 적용하기
    private final GoodsMapper goodsMapper;
    private final GoodsService goodsService;
    private final CategoryMapper categoryMapper;

    @Override
    public int countByKeyword(String keyword) {
        return goodsMapper.countGoodsByKeyword(keyword);
    }

    @Override
    public SearchResponseDTO searchByKeyword(String keyword) {

        SearchResponseDTO result = new SearchResponseDTO();

        String sort = "latest";
        int page = 1;
        int size = 20;
        int offset = 0;


        Map<String, Object> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("sort", sort);
        params.put("offset", offset);
        params.put("size", size);

        long countStartTime = System.currentTimeMillis();

        // TODO : 페이지네이션 할 때는 DB 조회 안하도록 수정하기 -> 캐시처리?
        int totalCount = countByKeyword(keyword);

        long countEndTime = System.currentTimeMillis();
        long totalCountTime = countEndTime - countStartTime;
        log.info("검색어: '{}' count 완료 소요 시간: {} ms", keyword, totalCountTime);

        long searchStartTime = System.currentTimeMillis();

        List<Goods> searchResults = goodsMapper.findGoodsByKeyword(params);

        long searchEndTime = System.currentTimeMillis();
        long totalSearchTime = searchEndTime - searchStartTime;
        log.info("검색어: '{}' 검색 소요 시간 : {} ms", keyword, totalSearchTime);

        long totalTime = totalCountTime + totalSearchTime;
        log.info("검색어: '{}' 검색에 들어간 총 소요 시간: {} ms", keyword, totalTime);

        int totalPages = (int) Math.ceil((double) totalCount / size);

        // TODO : 찜 상태는 완성되면 추가

        // TODO : 중복되는 코드 메소드화 하거나 유틸 클래스로 빼기
        int maxPageNumbersToShow = 10;
        int startPage;
        int endPage;

        if (totalPages <= maxPageNumbersToShow) {
            startPage = 1;
            endPage = totalPages;
        } else {
            if (page <= 6) {
                startPage = 1;
                endPage = 10;
            } else if (page + 4 >= totalPages) {
                startPage = totalPages - 9;
                endPage = totalPages;
            } else {
                startPage = page - 5;
                endPage = page + 4;
            }
        }

        // 대표 이미지 가져오기
        for (Goods goods : searchResults) {
            GoodsImage mainImage = goodsService.getMainImageByGoodsNo(goods.getGoodsNo());
            goods.setMainImage(mainImage);
        }

        // 카테고리별 갯수 세는 로직
        List<CategoryCountDTO> countCategories = goodsMapper.countCategoryByKeyword(keyword);
        Map<Category, Integer> categoryList = new HashMap<>();
        for(CategoryCountDTO countCategory : countCategories) {
            int cateNo = countCategory.getCateNo();
            Category category = categoryMapper.findTitle(cateNo);
            categoryList.put(category, countCategory.getCount());
        }

        Map<Category, Integer> sortedCategoryMap = categoryList.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,       // 중복 key 발생 시 기존 값을 유지
                        LinkedHashMap::new    // 순서를 유지하기 위해 LinkedHashMap 사용
                ));

        // goodsPrice 필드를 int 매핑하여 최댓값과 최솟값을 구함
        OptionalInt maxPriceOpt = searchResults.stream()
                .mapToInt(Goods::getGoodsPrice)
                .max();

        OptionalInt minPriceOpt = searchResults.stream()
                .mapToInt(Goods::getGoodsPrice)
                .min();


        // OptionalInt 값이 존재하는 경우에만 값을 가져옴
        if (maxPriceOpt.isPresent()) {
            int maxPrice = maxPriceOpt.getAsInt();
            int minPrice = minPriceOpt.getAsInt();
            result.setMaxPrice(maxPrice);
            result.setMinPrice(minPrice);
        }

        result.setSearchResults(searchResults);
        result.setKeyword(keyword);
        result.setTotalCount(totalCount);
        result.setCategoryList(sortedCategoryMap);

        result.setSort(sort);
        result.setCurrentPage(page);
        result.setTotalPages(totalPages);
        result.setStartPage(startPage);
        result.setEndPage(endPage);
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

        int totalPages = (int) Math.ceil((double) totalCount / size);

        int maxPageNumbersToShow = 10;
        int startPage;
        int endPage;

        if (totalPages <= maxPageNumbersToShow) {
            startPage = 1;
            endPage = totalPages;
        } else {
            if (page <= 6) {
                startPage = 1;
                endPage = 10;
            } else if (page + 4 >= totalPages) {
                startPage = totalPages - 9;
                endPage = totalPages;
            } else {
                startPage = page - 5;
                endPage = page + 4;
            }
        }

        result.setSearchResults(searchResults);
        result.setKeyword(result.getKeyword());
        result.setSort(sort);
        result.setCurrentPage(page);
        result.setTotalPages(totalPages);
        result.setSize(size);
        result.setStartPage(startPage);
        result.setEndPage(endPage);
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

        int totalCount = goodsList.size();

        int totalPages = (int)Math.ceil((double)totalCount / searchParamDTO.getSize());


        int maxPageNumbersToShow = 10;
        int startPage;
        int endPage;

        if (totalPages <= maxPageNumbersToShow) {
            startPage = 1;
            endPage = totalPages;
        } else {
            if (page <= 6) {
                startPage = 1;
                endPage = 10;
            } else if (page + 4 >= totalPages) {
                startPage = totalPages - 9;
                endPage = totalPages;
            } else {
                startPage = page - 5;
                endPage = page + 4;
            }
        }

        SearchResponseDTO result = new SearchResponseDTO();

        // 카테고리별 갯수 세는 로직
        Map<Category, Integer> categoryList = new HashMap<>();
        for(Goods goods : goodsList) {
            int cateNo = goods.getCateNo();
            Category category = categoryMapper.findTitle(cateNo);
            categoryList.merge(category, 1, Integer::sum);
        }

        // 재검색 키워드가 존재할 경우 검색 결과를 2차 필터랑 하는 로직
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

        // 해당 검색 결과의 최대 가격, 최소 가격 구하는 로직


        result.setSearchResults(goodsList);
        result.setTotalCount(totalCount);
        result.setCurrentPage(page);
        result.setTotalPages(totalPages);
        result.setStartPage(startPage);
        result.setEndPage(endPage);
        result.setSort(searchParamDTO.getSort());
        result.setSize(searchParamDTO.getSize());
        result.setCategoryList(categoryList);

        return result;
    }

}
