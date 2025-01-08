package hello.fclover.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Seller {

    private Long id;                // `id` (auto incremented primary key)
    private String sellerId;        // `seller_id` (varchar(20))
    private String sellerPass;      // `seller_pass` (varchar(50))
    private String sellerName;      // `seller_name` (varchar(100))
    private String sellerEmail;     // `seller_email` (varchar(100))
    private String sellerPhone;     // `seller_phone` (varchar(20))
    private String sellerNumber;    // `seller_number` (varchar(200))
    private String companyName;     // `company_name` (varchar(20))
    private String admissionAt;     // `admisson_at` (varchar(200))
    private String sellerBirth;     // `seller_birth` (varchar(8)) (YYYYMMDD)
    private String sellerGender;    // `seller_gender` (varchar(1)) ('M' or 'F')
    private LocalDateTime createAt;     // `create_at` (timestamp)
    private LocalDateTime updatedAt;    // `updated_at` (timestamp)


}
