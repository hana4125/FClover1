package hello.fclover.service;

import hello.fclover.domain.Delivery;
import hello.fclover.domain.Payment;
import hello.fclover.domain.Seller;

import java.util.List;

public interface BackOfficeService {

    List<Payment> searchOrder();

    List<Seller> searchSeller();

    void InsertdeliveryReadyList(Long orderId, String userId);

    List<Delivery> deliveryReadyOrderSearch();
}
