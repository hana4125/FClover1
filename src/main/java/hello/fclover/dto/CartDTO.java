package hello.fclover.dto;

import hello.fclover.domain.GoodsImage;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CartDTO {

    private Long cartNo;
    private Long memberNo;
    private Long goodsNo;
    private int cartQuantity;
    private LocalDateTime createdAt;
    private LocalDateTime deliveryDate;
    private String goodsName;
    private int goodsPrice;
    private GoodsImage mainImage;

}
