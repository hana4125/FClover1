package hello.fclover.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

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

    public Member(){

    }

    public Member(Object memberNo, String memberId, String password, String name, String email, String birthdate, String phoneNumber, String gender, String auth, Object profilePicture, Object createdAt, Object updatedAt) {
        this.memberNo = (Long) memberNo;
        this.memberId = memberId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.birthdate = birthdate;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.auth = auth;
        this.profilePicture = (String)profilePicture;
        this.createdAt = (LocalDateTime) createdAt;
        this.updatedAt = (LocalDateTime) updatedAt;


    }
}
