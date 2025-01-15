package hello.fclover.controller;

import hello.fclover.domain.Goods;
import hello.fclover.domain.Member;
import hello.fclover.service.GoodsService;
import hello.fclover.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Slf4j
@Controller
@RequestMapping(value = "/payments")
@RequiredArgsConstructor
public class PaymentsController {

    private final MemberService memberService;
    private final GoodsService goodsService;

    @ModelAttribute("member")
    public Member addMemberToModel(Principal principal) {

        if (principal != null) {
            String memberId = principal.getName();
            return memberService.findMemberById(memberId);
        }
        return null;
    }

    //바로구매 페이지로 랜딩
    @GetMapping
    public String OrderListDetail(@RequestParam("goodsNo") Long goodsNo, Model model) {

        Goods goods = goodsService.findGoodsByNo(goodsNo);

        model.addAttribute("goods", goods);

        return "user/userPayments";
    }
}
