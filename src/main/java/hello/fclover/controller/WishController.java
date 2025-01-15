package hello.fclover.controller;

import hello.fclover.domain.Member;
import hello.fclover.service.MemberService;
import hello.fclover.service.WishService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/wish")
@RequiredArgsConstructor
public class WishController {

    private final WishService wishService;
    private final MemberService memberService;

    /**
     * 로그인 상태라면, Principal(= memberId)로 DB에서 Member 객체를 조회하여
     * ModelAttribute로 주입한다. 미로그인이면 null 반환.
     */
    @ModelAttribute("member")
    public Member addMemberToModel(Principal principal) {
        if (principal != null) {
            String memberId = principal.getName();
            // DB에서 Member 조회
            return memberService.findMemberById(memberId);
        }
        return null; // 미로그인 시 null
    }

    @PostMapping("/wishlist")
    public Map<String, String> toggleWishlist(
            @ModelAttribute("member") Member member,
            @RequestParam("goodsNo") Long goodsNo
    ) {
        // (1) 로그인하지 않은 경우 처리
        if (member == null) {
            // 상황에 따라 예외를 던지거나, 메시지/에러코드 응답
            throw new IllegalStateException("로그인되지 않은 사용자입니다.");
        }

        // (2) memberNo를 꺼내서 로직에 활용
        Long memberNo = (long) member.getMemberNo(); // Member 엔티티 안의 PK(또는 식별자)

        // (3) 이미 찜했는지 확인
        boolean isWished = wishService.checkWished(goodsNo, memberNo);

        String status;
        if (!isWished) {
            // (4) 찜 목록에 추가
            wishService.addWishlist(goodsNo, memberNo);
            status = "added";  // 프론트에서 "찜 설정 되었습니다." 처리
        } else {
            // (5) 찜 목록에서 제거
            wishService.removeWishlist(goodsNo, memberNo);
            status = "removed"; // 프론트에서 "찜 해제 되었습니다." 처리
        }

        // (6) 응답 생성
        Map<String, String> result = new HashMap<>();
        result.put("status", status);
        return result;
    }
}
