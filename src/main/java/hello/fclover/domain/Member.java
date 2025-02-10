package hello.fclover.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Member {

    private Long memberNo;
    private String memberId;
    private String password;
    private String name;
    private String email;
    private String birthdate;
    private String phoneNumber;
    private String gender;
    private String auth;
    private String profilePicture;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
