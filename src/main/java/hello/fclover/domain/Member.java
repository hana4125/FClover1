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

    private int NOTI_NUM;
    private String NOTI_TITLE;
    private String NOTI_NAME;
    private String NOTI_CONTENT;
    private int NOTI_RE_REF;
    private int NOTI_RE_LEV;
    private int NOTI_RE_SEQ;
    private String NOTI_DATE;

}
