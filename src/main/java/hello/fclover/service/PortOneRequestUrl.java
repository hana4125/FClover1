package hello.fclover.service;


import ch.qos.logback.classic.html.UrlCssBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PortOneRequestUrl {
//    public static final UrlCssBuilder ACCESS_TOKEN_URL = ;


//    ACCESS_TOKEN_URL("/users/getToken");
//    CANCEL_PAYMENT_URL("/payments/cancel"),
//    CREATE_PAYMENT_URL("/payments/");

    private final String url;
}
