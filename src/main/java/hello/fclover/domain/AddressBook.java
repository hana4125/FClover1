package hello.fclover.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddressBook {

    private Long addressNo;
    private Long memberNo;
    private String addressName;
    private String recipientName;
    private String phoneNumber;
    private String address;
    private boolean isDefault;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
