package hello.fclover.service;

import hello.fclover.domain.Seller;

public interface SellerService {

    int signup(Seller seller);

    Seller findSellerById(String sellerId);

    String isSellerIdDuplicate(String sellerId);
}
