package hello.fclover.service;


import hello.fclover.domain.Seller;
import hello.fclover.domain.Goods;
import hello.fclover.domain.Settlement;

import java.util.List;
import java.util.Map;

public interface SellerService {
    int signup(Seller seller);

    Seller findSellerById(String sellerId);

    String isSellerIdDuplicate(String sellerId);

    List<Map<String, Object>> getListDetail(int n, String searchWord);

    int getSearchListCount(String search);

    List<Seller> getSearchList(String search, int page, int limit);

    List<Settlement> searchDaySettlement(Long partnerId);
}
