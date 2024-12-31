package hello.fclover;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FCloverApplication {

    public static void main(String[] args) {

        // 루트 디렉토리에 .env 파일 만들어서 Client Id & Client Secret 관리하기 (gitignore)
        Dotenv dotenv = Dotenv.configure().load();

        System.setProperty("GOOGLE_CLIENT_ID", dotenv.get("GOOGLE_CLIENT_ID"));
        System.setProperty("GOOGLE_CLIENT_SECRET", dotenv.get("GOOGLE_CLIENT_SECRET"));
        System.setProperty("NAVER_CLIENT_ID", dotenv.get("NAVER_CLIENT_ID"));
        System.setProperty("NAVER_CLIENT_SECRET", dotenv.get("NAVER_CLIENT_SECRET"));

        SpringApplication.run(FCloverApplication.class, args);
    }

}
