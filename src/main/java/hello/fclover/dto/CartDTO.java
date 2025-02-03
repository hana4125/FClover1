package hello.fclover.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CartDTO {

    Long cartNo;
    Long memberNo;
    Long goodsNo;
    int cartQuantity;
    LocalDateTime createdAt;
    LocalDateTime deliveryDate;
    String goodsName;
    int goodsPrice;

}
