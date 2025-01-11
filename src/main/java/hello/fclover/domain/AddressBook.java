package hello.fclover.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddressBook {

    private int addressNo;
    private int memNo;
    private String addressName;
    private String recipientName;
    private String phoneNumber;
    private String address;
    private boolean isDefault;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
