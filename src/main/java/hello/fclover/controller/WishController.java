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

    @ModelAttribute("member")
    public Member addMemberToModel(Principal principal) {
        if (principal != null) {
            String memberId = principal.getName();
            return memberService.findMemberById(memberId);
        }
        return null;
    }

    @PostMapping("/wishlist")
    public Map<String, String> toggleWishlist(
            @ModelAttribute("member") Member member,
            @RequestParam("goodsNo") Long goodsNo
    ) {
        // memberNo를 꺼내서 로직에 활용
        Long memberNo = member.getMemberNo(); // Member 엔티티 안의 PK(또는 식별자)

        // 이미 찜했는지 확인
        boolean isWished = wishService.checkWished(goodsNo, memberNo);

        String status;
        if (!isWished) {
            // 찜 목록에 추가
            wishService.addWishlist(goodsNo, memberNo);
            status = "added";  // 프론트에서 "찜 설정 되었습니다." 처리
        } else {
            // 찜 목록에서 제거
            wishService.removeWishlist(goodsNo, memberNo);
            status = "removed"; // 프론트에서 "찜 해제 되었습니다." 처리
        }

        Map<String, String> result = new HashMap<>();
        result.put("status", status);
        return result;
    }
}
