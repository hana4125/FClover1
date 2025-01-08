package hello.fclover.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Member {

    private int memNum;
    private String memberId;
    private String password;
    private String name;
    private String email;
    private String phoneNumber;
    private String auth;
    private String profilePicture;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
