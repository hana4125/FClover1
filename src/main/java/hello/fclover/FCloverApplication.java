package hello.fclover;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestClient;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class FCloverApplication {

    public static void main(String[] args) {
        SpringApplication.run(FCloverApplication.class, args);
    }

    @Bean
    public RestClient restClient(){
        return RestClient.create();
    }
}
