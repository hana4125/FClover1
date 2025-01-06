package hello.fclover.service;

import hello.fclover.domain.Payment;

import java.util.List;

public interface BackOfficeService {

    List<Payment> searchOrder();

    int getProduct_stock(int goods_no);

    int decrease(int stockCount);
}
