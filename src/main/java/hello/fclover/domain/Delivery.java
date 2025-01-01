package hello.fclover.domain;

import lombok.Data;

@Data
public class Delivery {

    String member_id;
    String shipping_address_name;
    String receiver;
    String phone;
    String address;
}
