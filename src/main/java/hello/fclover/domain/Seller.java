package hello.fclover.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Seller {

    private Long sellerNo;                // `id` (auto incremented primary key)
    private String sellerId;        // `seller_id` (varchar(20))
    private String password;      // `seller_pass` (varchar(50))
    private String name;      // `seller_name` (varchar(100))
    private String email;     // `seller_email` (varchar(100))
    private String phoneNumber;     // `seller_phone` (varchar(20))
    private String businessNumber;    // `seller_number` (varchar(200))
    private String companyName;     // `company_name` (varchar(20))
    private String admissionAt;     // `admisson_at` (varchar(200))
    private LocalDateTime createAt;     // `create_at` (timestamp)
    private LocalDateTime updatedAt;    // `updated_at` (timestamp)

}
