package hello.fclover.service;

import hello.fclover.domain.Payment;
import hello.fclover.domain.PaymentReq;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

@Service
public interface PaymentService {

    List<Payment> searchList(String userId);

    Payment save(PaymentReq paymentReq);

    int savePayment(Payment Payment);

    void cancelPayment(String uid);

    HttpURLConnection createConnection(String urlString) throws Exception;

    boolean isConnectionSuccessful(HttpURLConnection connection);
}
