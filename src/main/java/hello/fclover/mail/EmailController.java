package hello.fclover.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class EmailController {

    private final EmailService emailService;

    @ResponseBody
    @PostMapping("/send-email")
    public String sendEmail() {
        EmailMessage emailMessage = EmailMessage.builder()
                .to("sinmagic1@naver.com")
                .subject("테스트메일")
                .message("<h1>비밀번호 재설정을 위한 이메일입니다.</h1>")
                .build();
        emailService.sendMail(emailMessage);
        return "success";
    }
}
