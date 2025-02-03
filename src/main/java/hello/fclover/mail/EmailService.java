package hello.fclover.mail;

import hello.fclover.domain.Question;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Value("${spring.mail.username}")
    private String from;

    public static String generateRandomNumber() {
        Random random = new Random();
        StringBuilder randomNumber = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            int digit = random.nextInt(10); // 0부터 9까지의 랜덤 숫자
            randomNumber.append(digit);
        }

        return randomNumber.toString();
    }

    // 비동기 방식
    public void asyncSendMail(EmailMessage emailMessage) {
        CompletableFuture.runAsync(() -> sendMail(emailMessage))
                .exceptionally(throwable -> {
                    log.error("Exception occurred: {}", throwable.getMessage());
                    return null;
                });
    }



    // 동기 방식
    public void sendMail(EmailMessage emailMessage)  {
        MimeMessage mimeMessage = mailSender.createMimeMessage(); // MimeMessage 객체 생성
        try {
            // MimeMessageHelper를 사용하여 보다 쉽게 MimeMessage를 구성할 수 있다.
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

            // 이메일 수신자 설정
            mimeMessageHelper.setTo(emailMessage.getTo());

            // 이메일 제목 설정
            mimeMessageHelper.setSubject(emailMessage.getSubject());

            // 본문 내용 설정, false는 HTML 형식의 메세지를 사용하지 않음을 나타낸다.
            mimeMessageHelper.setText(emailMessage.getMessage(), true);

            // 이메일 발신자 설정
            mimeMessageHelper.setFrom(new InternetAddress(from + "@naver.com" ,"네잎클로버", "UTF-8"));

            // 이메일 보내기
            mailSender.send(mimeMessage);

        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

}
