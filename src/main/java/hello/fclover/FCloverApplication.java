package hello.fclover;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestClient;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class FCloverApplication {

    public static void main(String[] args) {
        SpringApplication.run(FCloverApplication.class, args);
    }

    @Bean
    public RestClient restClient(){
        return RestClient.create();
    }
}
