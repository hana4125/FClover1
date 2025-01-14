package hello.fclover.mail;/*
package hello.fclover.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service

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

    public void sendMail(EmailMessage emailMessage) {
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
            mimeMessageHelper.setFrom(new InternetAddress(from + "@naver.com"));

            // 이메일 보내기
            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
*/
