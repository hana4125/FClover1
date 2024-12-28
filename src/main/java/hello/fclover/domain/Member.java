package hello.fclover.domain;

import lombok.Data;

@Data
public class Member {

    private String member_id;
    private String password;
    private String name;
    private String email;
    private String phone;
    private String auth;
}
