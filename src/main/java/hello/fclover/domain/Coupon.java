package hello.fclover.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {
        private long couponId;
        private String memberId;
        private String couponName;
        private long couponAmount;
        private LocalDateTime couponCreatedAt;
        private LocalDateTime couponExpire;

    }
