package hello.fclover.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Goods {
    private int goods_no;
    private String seller_id;
    private int cate_no;
    private String goods_name;
    private String goods_content;
    private int goods_price;
    private String goods_writer;
    private String seller_company;
    private String goods_create_at;
    private String goods_image;
    private String goods_save_name;
    private int goods_count;
    private String goods_date;
    private int goods_pageCount;
    private String goods_bookSize;
    private String writer_content;
    private String created_at;
    private String updated_at;
}
