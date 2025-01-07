package hello.fclover.domain;

import lombok.Data;

@Data
public class Goods {
    private int goods_no;
    private int cate_no;
    private String goods_name;
    private int goods_price;
    private String goods_writer;
    private String seller_company;
    private String goods_create_at;
    private String goods_image;
    private int goods_count;
}
