package hello.fclover.service;

import hello.fclover.domain.Payment;
import hello.fclover.domain.Seller;

import java.util.List;

public interface BackOfficeService {

    List<Payment> searchOrder();

    List<Seller> searchSeller();

//    int getProduct_stock(int goods_no);
//
//    int decrease(int stockCount);
}
