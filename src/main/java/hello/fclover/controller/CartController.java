package hello.fclover.controller;

import hello.fclover.domain.Member;
import hello.fclover.service.CartService;
import hello.fclover.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final MemberService memberService;

    @ModelAttribute("member")
    public Member addMemberToModel(Principal principal) {
        if (principal != null) {
            String memberId = principal.getName();
            return memberService.findMemberById(memberId);
        }
        return null;
    }

    @PostMapping("/cartList")
    public Map<String, String> addToCart(@ModelAttribute("member") Member member,
                                         @RequestParam("goodsNo") Long goodsNo) {
        // 로그인한 회원의 PK
        Long memberNo = member.getMemberNo();

        // Upsert(이미 있으면 수량 +1, 없으면 Insert)
        int affectedRows = cartService.upsertCart(goodsNo, memberNo);

        // MySQL에서 ON DUPLICATE KEY UPDATE 시
        // 1) Insert 발생 시 → Affected Rows = 1
        // 2) Update(값 변경) 발생 시 → Affected Rows = 2
        // (만약 기존 값과 동일해 실제 업데이트가 없으면 0이 될 수도 있습니다만,
        //  여기서는 2인 경우를 "updated"로 간주)

        String status;
        if (affectedRows == 1) {
            // Insert 되었다고 보고
            status = "added";
        } else {
            // Update 되었다고 보고 (2, 혹은 0)
            status = "updated";
        }

        // JSON 형태로 응답
        Map<String, String> result = new HashMap<>();
        result.put("status", status);
        return result;
    }
}

