package hello.fclover.service;

import hello.fclover.domain.Goods;
import hello.fclover.domain.GoodsImage;
import hello.fclover.dto.SearchDetailParamDTO;
import hello.fclover.dto.SearchParamDTO;
import hello.fclover.dto.SearchResponseDTO;
import hello.fclover.mybatis.mapper.GoodsMapper;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final GoodsMapper goodsMapper;
    private final GoodsService goodsService;

    @Override
    public int countByKeyword(String keyword) {
        return goodsMapper.countGoodsByKeyword(keyword);
    }

    @Override
    public Map<String, Object> searchByKeyword(String keyword, String sort, int offset, int size) {
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

        Map<String, Object> result = new HashMap<>();
        result.put("totalCount", totalCount);
        result.put("searchResults", searchResults);

        return result;
    }

    @Override
    public Map<String, Object> searchDetail(SearchDetailParamDTO searchDetailParamDTO, String sort, int offset, int size) {
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

        Map<String, Object> result = new HashMap<>();
        result.put("totalCount", totalCount);
        result.put("searchResults", searchResults);

        return result;
    }

    @Override
    public SearchResponseDTO refineResult(SearchParamDTO searchParamDTO) {
        int page = searchParamDTO.getPage();
        int offset = (page - 1) * searchParamDTO.getSize();
        List<Goods> goodsList = goodsMapper.searchByParam(searchParamDTO, offset);

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
        result.setSearchResults(goodsList);
        result.setTotalCount(totalCount);
        result.setCurrentPage(page);
        result.setTotalPages(totalPages);
        result.setStartPage(startPage);
        result.setEndPage(endPage);
        result.setSort(searchParamDTO.getSort());
        result.setSize(searchParamDTO.getSize());

        return result;
    }

}
