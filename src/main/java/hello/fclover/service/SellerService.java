package hello.fclover.service;


import hello.fclover.domain.Seller;
import hello.fclover.domain.Goods;
public interface SellerService {
    int signup(Seller seller);

    Seller findSellerById(String sellerId);

    String isSellerIdDuplicate(String sellerId);

    Seller selectSellerByIdPassword(String sellerId, String password);
}
