package hello.fclover.service;


import hello.fclover.domain.Seller;

import java.util.List;
import java.util.Map;

public interface SellerService {
    int signup(Seller seller);

    Seller findSellerById(String sellerId);

    String isSellerIdDuplicate(String sellerId);



    List<Map<String, Object>> getListDetail(int n, String searchWord, int pageSize);
    int getListCount(String searchWord);

    int getSearchListCount(String search);
    List<Seller> getSearchList(String search, int page, int limit);


}
