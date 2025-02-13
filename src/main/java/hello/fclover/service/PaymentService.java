package hello.fclover.service;

import hello.fclover.domain.Payment;
import hello.fclover.domain.PaymentReq;
import hello.fclover.dto.PaymentGoodsDTO;
import hello.fclover.dto.PaymentGoodsImageDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

@Service
public interface PaymentService {

    List<PaymentGoodsDTO> searchList(String userId);

    Payment save(PaymentReq paymentReq);

    int savePayment(Payment Payment);

    void cancelPayment(String uid);

    HttpURLConnection createConnection(String urlString) throws Exception;

    boolean isConnectionSuccessful(HttpURLConnection connection);

    PaymentGoodsDTO searchOneOrderDetail(String name, Long orderId);

    List<PaymentGoodsImageDTO> searchPaymentGoodsImage(String name);

    PaymentGoodsImageDTO searchOneOrderPaymentGoodsImage(String name, Long orderId);
}
