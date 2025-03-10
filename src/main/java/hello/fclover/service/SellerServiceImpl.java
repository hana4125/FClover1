package hello.fclover.service;

import hello.fclover.domain.Seller;
import hello.fclover.domain.Settlement;
import hello.fclover.mybatis.mapper.SellerMapper;
import hello.fclover.mybatis.mapper.SettlementMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {

    private final SellerMapper dao;
    private final SettlementMapper settlementMapper;
    private final SellerMapper sellerMapper;

    public static Settlement create(Long partnerId, BigDecimal totalAmount, LocalDate paymentDate) {
        Settlement settlement = new Settlement();
        settlement.setSellerId(partnerId);
        settlement.setTotalAmount(totalAmount);
        settlement.setStatus("completed");
        settlement.setPaymentDate(paymentDate);
        settlement.setCreatedAt(LocalDateTime.now());
        settlement.setUpdatedAt(LocalDateTime.now());
        return settlement;
    }

    @Override
    public int signup(Seller seller) {
        return dao.insertSeller(seller);
    }

    @Override
    public Seller findSellerById(String sellerId) {
        return dao.selectSellerById(sellerId);
    }

    @Override
    public String isSellerIdDuplicate(String sellerId) {
        return dao.isSellerIdDuplicate(sellerId);
    }

    @Override
    public List<Map<String, Object>> getListDetail(int page, String searchWord, int pageSize ,long sellerNo,String searchField) {
        Map<String, Object> params = new HashMap<>();
        params.put("searchField", searchField.split(""));
        params.put("searchWord", searchWord);
        params.put("sellerNo", sellerNo);
        params.put("pageSize", pageSize);  // 페이지 크기 반영
        params.put("offset", (page - 1) * pageSize);  // 페이지네이션 적용
        return dao.getListDetail(params);
    }

    @Override
    public int getListCount(String searchWord, long sellerNo, String searchField) {
        Map<String, Object> params = new HashMap<>();
        params.put("searchField", searchField.split(""));
        params.put("searchWord", searchWord);
        params.put("sellerNo", sellerNo);
        return dao.getListCount(params);
    }


    @Override
    public List<Settlement> searchDaySettlement(String sellerName) {

        Long partnerId = sellerMapper.getSellerId(sellerName);
        List<Settlement> settlements = settlementMapper.searchDaySettlement(partnerId);

        return settlements;
    }

    @Override
    public long getselectNo(String sellerId) {
        return dao.getselectNo(sellerId);
    }


}
