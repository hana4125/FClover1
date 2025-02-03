package hello.fclover.service;

import hello.fclover.domain.Payment;
import hello.fclover.domain.PaymentReq;
import hello.fclover.mybatis.mapper.PaymentMapper;
import hello.fclover.util.PaymentClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentMapper dao;
    private final PaymentClient paymentClient;
    private final PaymentMapper paymentMapper;

    @Transactional(readOnly = true)
    @Override
    public List<Payment> searchList(String userId) {
        List<Payment> payments =  dao.searchList(userId);

        return payments;
    }

    @Transactional
    @Override
    public Payment save(PaymentReq paymentReq) {
       Payment payment1 = dao.save(paymentReq);



        return payment1;
    }

    @Transactional
    @Override
    public int savePayment(Payment payment) {
        int result = dao.savePayment(payment);

            return result;

    }

    @Transactional
    @Override
    public void cancelPayment(String uid) {

        // 외부 API로 결제 취소 요청
        String result = paymentClient.cancelPayment(uid);

        if (result.contains("ERROR")) {
            throw new RuntimeException("Payment cancellation failed during recovery process.");
        }

        // impUid로 Payment 테이블 조회
        Payment payment  = dao.findByImpUid(uid);

        // status 필드를 "cancel"로 변경
        payment.setStatus("cancel");
    }

    @Transactional
    @Override
    public HttpURLConnection createConnection(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // 타임아웃 설정
        connection.setConnectTimeout(10000);  // 연결 타임아웃 10초
        connection.setReadTimeout(10000);     // 읽기 타임아웃 10초

        //각각의 호출상황에 맞게 수정하기.

        return connection;
    }

    @Transactional
    @Override
    public boolean isConnectionSuccessful(HttpURLConnection connection) {
        try {
            // 연결 확인
            int responseCode = connection.getResponseCode();

            // 연결이 성공적인 경우
            if (responseCode == HttpURLConnection.HTTP_OK) {  // 200 OK
                return true;
            } else {
                System.out.println("Connection failed with response code: " + responseCode);

                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
