package hello.fclover.service;

import hello.fclover.domain.Seller;
import hello.fclover.domain.Settlement;
import hello.fclover.mybatis.mapper.SellerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {

    private final SellerMapper dao;

    public static Settlement create(Long partnerId, BigDecimal totalAmount, LocalDate paymentDate) {
        Settlement settlement = new Settlement();
        settlement.setPartnerId(partnerId);
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
    public List<Map<String, Object>> getListDetail(int n, String searchWord) {
        Map<String, Object> params = new HashMap<>();
        params.put("searchWord", searchWord);
        params.put("pageSize", 10);  // 페이지 사이즈
        params.put("offset", (n - 1) * 10);  // offset 계산

        return dao.getListDetail(params);
    }


}
