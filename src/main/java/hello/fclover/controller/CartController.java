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
@RequestMapping("/member")
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

    @PostMapping("/cart")
    @ResponseBody
    public Map<String, String> addToCart(@RequestParam("goodsNo") Long goodsNo,
                                         Principal principal) {
        String memberNo = principal.getName();

        // 장바구니에 이미 해당 상품이 존재하는지 확인
        boolean isExist = cartService.checkExistInCart(goodsNo, Long.valueOf(memberNo));

        String status;
        if (!isExist) {
            // 장바구니 신규 추가
            cartService.addCart(goodsNo, Long.valueOf(memberNo));
            status = "added";   // "상품이 장바구니에 담겼습니다." 처리
        } else {
            // 이미 있는 상품이면 수량을 추가
            cartService.updateCart(goodsNo, Long.valueOf(memberNo));
            status = "updated"; // "장바구니에 이미 담은 상품이 있어 수량이 추가 되었습니다." 처리
        }

        Map<String, String> result = new HashMap<>();
        result.put("status", status);
        return result;
    }
}

