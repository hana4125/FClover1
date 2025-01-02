package hello.fclover.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PortOneRequestUrl {
    ACCESS_TOKEN_URL("/users/getToken"),
    CANCEL_PAYMENT_URL("/payments/cancel"), //jpa table name이 payments
    CREATE_PAYMENT_URL("/payments/"); //payments 테이블 생성

    private final String url;
}
