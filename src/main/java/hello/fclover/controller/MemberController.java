package hello.fclover.controller;

import hello.fclover.domain.Delivery;
import hello.fclover.domain.Member;
import hello.fclover.domain.Payment;
import hello.fclover.domain.PaymentReq;
import hello.fclover.service.CommentService;
import hello.fclover.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import hello.fclover.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.security.Principal;
import hello.fclover.domain.PaginationResult;

@Slf4j
@Controller
@RequestMapping(value="/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService   memberService;
    private final PasswordEncoder passwordEncoder;
    private final PaymentService paymentService;
    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    @GetMapping("/signup")
    public String signup() {
        return "user/userSignup";
    }

    @PostMapping("/signupProcess")
    public String signupProcess(@ModelAttribute Member member) {

        String encPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encPassword);

        int result = memberService.signup(member);

        if (result == 1) {
            log.info("회원가입 완료");
            return "redirect:/";
        } else {
            log.info("회원가입 실패");
            return "error/error";
        }
    }

    @GetMapping("/login")
    public String login(@CookieValue(required = false) String rememberId, HttpSession session, Model model) {

        if (rememberId != null) {
            model.addAttribute("rememberId", rememberId);
        }

        model.addAttribute("message", session.getAttribute("loginfail"));
        session.removeAttribute("loginfail");

        return "user/userLogin";
    }

    @GetMapping("/myPage")
    public String myPageMain(Principal principal, Model model) {

        if (principal == null) {
            return "redirect:/member/login";
        }

        return "/user/mypage/userMyPageMain";
    }

    @GetMapping("/myPage/deliveryAddressBook")
    public String myPageDeliveryAddressBook(Principal principal, Model model) {
        String member_id = principal.getName();
        Member member = memberService.getMember(member_id);
        memberService.getDeliveryAddress(member_id);
        List<Delivery> deliveryAddressList = memberService.getDeliveryAddress(member_id);
        model.addAttribute("member", member);
        model.addAttribute("deliveryAddressList", deliveryAddressList);
        return "/user/mypage/userMyPageDeliveryAddressBook";
    }

    @PostMapping("/addDeliveryAddress")
    public String addDeliveryAddress(@ModelAttribute Delivery delivery) {
        memberService.addDeliveryAddress(delivery);
        return "redirect:/member/myPage/deliveryAddressBook";
    }

    @GetMapping("/myPage/info-check")
    public String temp(Model model, HttpSession session) {
        model.addAttribute("message", session.getAttribute("idCheckFail"));
        session.removeAttribute("idCheckFail");
        return "user/mypage/userMyPageInfoPasswordCheck";
    }

    @PostMapping("/myPage/info-check")
    public String passwordCheck(@RequestParam String password, Principal principal, HttpSession session) {
        String member_id = principal.getName();
        String encryptedPassword = memberService.getEncryptedPassword(member_id);

        boolean matches = passwordEncoder.matches(password, encryptedPassword);

        if (!matches) {
            session.setAttribute("idCheckFail", "입력하신 정보가 일치하지 않습니다. 다시 확인해 주세요.");
            return "redirect:/member/myPage/info-check";
        }

        return "redirect:/member/myPage/info";
    }

    @GetMapping("/myPage/info")
    public String myPageInfo(Principal principal, Model model) {
        String id = principal.getName();
        Member member = memberService.getMember(id);
        model.addAttribute("member", member);

        return "/user/mypage/userMyPageInfo";
    }

    @GetMapping("/myPage/info/modify")
    public String memberUpdateForm(Principal principal, Model model) {
        String id = principal.getName();
        Member member = memberService.getMember(id);
        model.addAttribute("member", member);

        return "/user/mypage/userMyPageInfoUpdateForm";
    }

    @GetMapping("/myPage/info/delete")
    public String deleteAccountForm() {
        return "user/mypage/userMyPageDeleteAccount";
    }

    @GetMapping("/myPage/info/deleteProgress")
    public String deleteAccount(Principal principal, HttpServletRequest request, HttpServletResponse response) {
        String member_id = principal.getName();
        memberService.removeAccount(member_id);
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/";
    }

    @GetMapping("/myPage/orderDelivery")
    public String myPageOrderDelivery() {
        return "/user/mypage/userMyPageOrderDelivery";
    }

    @GetMapping("/myPage/purchaseHistory")
    public String myPagePurchaseHistory() {
        return "/user/mypage/userMyPagePurchaseHistory";
    }

    @GetMapping("/myPage/wishlist")
    public String myPageWishlist() {
        return "/user/mypage/userMyPageWishlist";
    }

    @GetMapping("/cart")
    public String cart() {
        return "/user/userCart";
    }

    @PostMapping("/memberUpdate")
    public String memberUpdate(@ModelAttribute Member member) {
        String encPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encPassword);
        memberService.updateMember(member);
        return "redirect:/member/myPage/info";
    }

    @GetMapping("/memberPay")
    public String sellerPay() {
        return "user/userPayments";
    }

    @GetMapping("/memberPayDone")
    public String sellerPayDone() {
        return "user/userPaymentsDone";
    }

    @GetMapping("/memberOrderList")
    public String OrderList() {

        return "user/userOrderList";
    }
    @GetMapping("/memberOrderListDetail")
    public String OrderListDetail( Model model) {
//        @RequestParam String impUid,
//        model.addAttribute("impUid", impUid);

        return "user/userOrderListDetail";
    }


    @PostMapping("/portone")
    public ResponseEntity<Map<String, String>> savePortone(@RequestBody PaymentReq paymentRequest) {
        Map<String, String> response = new HashMap<>();
        System.out.println("========>controller의 paymentRequest : " + paymentRequest);

        try {
            System.out.println("========>controller의 try문 안의 paymentRequest : " + paymentRequest);
//            System.out.println("========>controller의 try문 안의 paymentService.savePayment(Payment.save(paymentRequest)) : " + paymentService.savePayment(Payment.save(paymentRequest)));

            paymentService.savePayment(Payment.save(paymentRequest));

//            System.out.println("========Controller====>Payment.save(paymentRequest)" + Payment.save(paymentRequest));
            response.put("message", "Payment processed successfully.");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Failed to process payment.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/OrderCancel/{uid}")
    public ResponseEntity<String> OrderCancel(@PathVariable("uid")String uid) {
        paymentService.cancelPayment(uid);

        return ResponseEntity.ok("Payment cancel processed successfully.");
    }

    @GetMapping("/notice")
    public String notice(
        @RequestParam(defaultValue = "1") int page, Model m) {

            int limit = 10;
            int listcount = memberService.getListCount();
            List<Member> list = memberService.getBoardList(page, limit);

            PaginationResult result = new PaginationResult(page, limit, listcount);

            m.addAttribute("page", page);
            m.addAttribute("maxpage", result.getMaxpage());
            m.addAttribute("startpage", result.getStartpage());
            m.addAttribute("endpage", result.getEndpage());
            m.addAttribute("listcount", listcount);
            m.addAttribute("boardlist", list);
            m.addAttribute("limit", limit);

            return "user/userNotice";
    }

    @GetMapping(value = "/notice/detail")
    public ModelAndView Detail(
            int num, ModelAndView mv,
            HttpServletRequest request,
            @RequestHeader(value = "referer", required = false) String beforeURL, HttpSession session) {

        String sessionReferer = (String) session.getAttribute("referer");
        logger.info("referer: " + beforeURL);
        if (sessionReferer != null && sessionReferer.equals("list")) {
            if (beforeURL != null && beforeURL.endsWith("list")) {
                memberService.setReadCountUpdate(num);
            }
            session.removeAttribute("referer");
        }

        Member member = memberService.getDetail(num);

        if (member == null) {
            logger.info("상세보기 실패");
            mv.setViewName("error/error");
            mv.addObject("url",request.getRequestURL());
            mv.addObject("message","상세보기 실패입니다.");
        }else {
            logger.info("상세보기 성공");
            //int count = CommentService.getListCount(num);
            mv.setViewName("user/userNoticeDetail");
            //mv.addObject("count",count);
            mv.addObject("noticedata", member);
        }
        return mv;
    }

    @GetMapping("/faq")
    public String question() {
        return "user/userQNA";
    }
}
