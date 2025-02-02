package hello.fclover.service;

import hello.fclover.domain.Goods;
import hello.fclover.dto.GoodsSearchParam;
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
    public Map<String, Object> searchDetail(GoodsSearchParam goodsSearchParam, String sort, int offset, int size) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", goodsSearchParam.getCname());
        params.put("writer", goodsSearchParam.getChrcDetail());
        params.put("companyName", goodsSearchParam.getPbcmDetail());

        params.put("cate", goodsSearchParam.getCate());

        params.put("minPrice", goodsSearchParam.getSaprmin());
        params.put("maxPrice", goodsSearchParam.getSaprmax());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate startDate = LocalDate.parse(goodsSearchParam.getRlseStr(), formatter);
        LocalDate endDate = LocalDate.parse(goodsSearchParam.getRlseEnd(), formatter);
        params.put("startDate", startDate);
        params.put("endDate", endDate);

        params.put("sort", sort);
        params.put("offset", offset);
        params.put("size", size);

        long countStartTime = System.currentTimeMillis();

        int totalCount = goodsMapper.countGoodsByParam(params);

        long countEndTime = System.currentTimeMillis();
        long totalCountTime = countEndTime - countStartTime;
        log.info("상세 검색어: '{}' 상세 count 완료 소요 시간: {} ms", goodsSearchParam.getRepKeyword(), totalCountTime);

        long searchStartTime = System.currentTimeMillis();

        List<Goods> searchResults = goodsMapper.findGoodsByParam(params);

        long searchEndTime = System.currentTimeMillis();
        long totalSearchTime = searchEndTime - searchStartTime;
        log.info("상세 검색어: '{}' 상세 검색 소요 시간 : {} ms", goodsSearchParam.getRepKeyword(), totalSearchTime);

        long totalTime = totalCountTime + totalSearchTime;
        log.info("상세 검색어: '{}' 상세 검색에 들어간 총 소요 시간: {} ms", goodsSearchParam.getRepKeyword(), totalTime);

        Map<String, Object> result = new HashMap<>();
        result.put("totalCount", totalCount);
        result.put("searchResults", searchResults);

        return result;
    }


}
