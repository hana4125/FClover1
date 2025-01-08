package hello.fclover.controller;

import hello.fclover.domain.*;
import hello.fclover.service.MemberService;
import hello.fclover.service.NoticeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import hello.fclover.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.security.Principal;
import java.util.List;

@Slf4j
@Controller
@RequestMapping(value="/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final NoticeService noticeService;
    private final PasswordEncoder passwordEncoder;
    private final PaymentService paymentService;

    @ModelAttribute("member")
    public Member addMemberToModel(Principal principal) {

        if (principal != null) {
            String memberId = principal.getName();
            return memberService.findMemberById(memberId);
        }
        return null;
    }

    @GetMapping("/signup")
    public String signup() {
        return "user/userSignup";
    }

    @PostMapping("/signupProcess")
    public String signupProcess(@ModelAttribute("signupMember") Member member) {

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
    public String myPageMain(Principal principal) {

        if (principal == null) {
            return "redirect:/member/login";
        }
        return "/user/mypage/userMyPageMain";
    }

    @GetMapping("/myPage/addressBook")
    public String myPageDeliveryAddressBook(Principal principal, Model model) {
        String memberId = principal.getName();
        Member member = memberService.findMemberById(memberId);
        int memNum = member.getMemNum();

        AddressBook defaultAddress = memberService.getDefaultAddress(memNum);
        List<AddressBook> addressBookList = memberService.getDeliveryAddress(memNum);

        model.addAttribute("defaultAddress", defaultAddress);
        model.addAttribute("addressBookList", addressBookList);

        return "user/mypage/userMyPageAddressBook";
    }

    @PostMapping("/addAddressBook")
    public String addDeliveryAddress(@ModelAttribute AddressBook addressBook, Principal principal) {
        String memberId = principal.getName();
        int memNum = memberService.getMemNum(memberId);
        addressBook.setMemNum(memNum);
        memberService.addDeliveryAddress(addressBook);
        return "redirect:/member/myPage/addressBook";
    }

    @GetMapping("/deleteAddressBook")
    public String deleteDeliveryAddress(@RequestParam int addressNum, RedirectAttributes redirectAttributes) {
        int result = memberService.checkDefaultAddress(addressNum);

        if (result == 1) {
            redirectAttributes.addFlashAttribute("message", "기본 배송지는 삭제하실수 없습니다.");
        } else {
            memberService.removeAddressBook(addressNum);
        }
        return "redirect:/member/myPage/addressBook";
    }



    @Transactional
    @PostMapping("/defaultAddress")
    public String defaultAddress(@RequestParam int addressNum) {
        memberService.setDefaultAddress(addressNum);
        return "redirect:/member/myPage/addressBook";
    }

    @GetMapping("/myPage/info-check")
    public String temp(Model model, HttpSession session) {
        model.addAttribute("message", session.getAttribute("idCheckFail"));
        session.removeAttribute("idCheckFail");
        return "user/mypage/userMyPageInfoPasswordCheck";
    }

    @PostMapping("/myPage/info-check")
    public String passwordCheck(@RequestParam String password, Principal principal, HttpSession session) {
        String memberId = principal.getName();
        String encryptedPassword = memberService.getEncryptedPassword(memberId);

        boolean matches = passwordEncoder.matches(password, encryptedPassword);

        if (!matches) {
            session.setAttribute("idCheckFail", "입력하신 정보가 일치하지 않습니다. 다시 확인해 주세요.");
            return "redirect:/member/myPage/info-check";
        }

        return "redirect:/member/myPage/info";
    }

    @GetMapping("/myPage/info")
    public String myPageInfo() {

        return "/user/mypage/userMyPageInfo";
    }

    @GetMapping("/myPage/info/modify")
    public String memberUpdateForm() {

        return "/user/mypage/userMyPageInfoUpdateForm";
    }

    @GetMapping("/myPage/info/delete")
    public String deleteAccountForm() {
        return "user/mypage/userMyPageDeleteAccount";
    }

    @GetMapping("/myPage/info/deleteProgress")
    public String deleteAccount(Principal principal, HttpServletRequest request, HttpServletResponse response) {
        String memberId = principal.getName();
        memberService.removeAccount(memberId);
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/";
    }

    @GetMapping("/myPage/profile")
    public String profile() {

        return "/user/mypage/userMyPageProfile";
    }

    @GetMapping("/myPage/delete-profile-picture")
    public String deleteProfile(Principal principal) {
        String memberId = principal.getName();
        memberService.removeProfilePicture(memberId);
        return "redirect:/member/myPage/profile";
    }

    @PostMapping("/myPage/upload-profile")
    public String uploadProfile(@RequestParam MultipartFile file, Principal principal) {

        String memberId = principal.getName();

        try {
            memberService.uploadProfilePicture(file, memberId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/member/myPage/profile";
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

    @GetMapping("/bestSeller")
    public String bestSeller() {
        return "/user/userBestseller";
    }

    @GetMapping("/steadySeller")
    public String steadySeller() {
        return "/user/userSteadyseller";
    }

    @GetMapping("/newItems")
    public String newItems() {
        return "/user/userNewItems";
    }

    @GetMapping("/sellerSignup")
    public String sellerSignup() {
        return "seller/sellerSignup";
    }

    @GetMapping("/sellerLogin")
    public String sellerLogin() {
        return "seller/sellerLogin";
    }

    @PostMapping("/memberUpdate")
    public String memberUpdate(@ModelAttribute Member member) {
        String encPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encPassword);
        memberService.updateMember(member);
        return "redirect:/member/myPage/info";
    }

    @PostMapping("/socialMemberUpdate")
    public String socialMemberUpdate(@ModelAttribute Member member) {
        String encPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encPassword);
        memberService.updateMember(member);
        return "redirect:/member/myPage/info";
    }

    @GetMapping("/memberPay")
    public String sellerPay(Principal principal,Model model) {
        model.addAttribute("username", principal.getName());
        return "user/userPayments";
    }

    @GetMapping("/memberPayDone")
    public String sellerPayDone() {
        return "user/userPaymentsDone";
    }


    //마이페이지 주문/배송 조회
    @GetMapping("/myPage/orderDelivery")
    public String myPageOrderDelivery(Principal principal, Model model) {
        List<Payment> payment = paymentService.searchList(principal.getName());
        System.out.println("principal = " + principal.getName());
        System.out.println("===========>여기는 controller ===============payment = " + payment);
        model.addAttribute("list", payment);
        return "/user/mypage/userOrderlist";
    }


    //마이페이지 주문/배송조회 상세보기
    @GetMapping("/myPage/memberOrderListDetail")
    public String OrderListDetail(@RequestParam("orderId") Long orderId, Model model) {

        System.out.println("======================>여기는 컨트롤러 : orderId = " + orderId);

        model.addAttribute("orderId", orderId);

        return "user/mypage/userOrderListDetail";
    }

    //포트원 결제
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

    //공지사항
    @GetMapping("/notice")
    public String notice(
            @RequestParam(defaultValue = "1") Integer page, Model m) {

        int limit = 10;
        int listcount = noticeService.getListCount();
        List<Member> list = noticeService.getBoardList(page, limit);

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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/notice/write")
    public String noticeWrite() {
        return "user/userWrite";
    }

    @PostMapping(value ="/notice/add")
    public String noticeAdd(Notice notice) {
        noticeService.insertNotice(notice);
        return "redirect:/member/notice";
    }


    @GetMapping(value = "/notice/detail")
    public String Detail(
            int num, ModelAndView mv,
            HttpServletRequest request,
            @RequestHeader(value = "referer", required = false) String beforeURL, HttpSession session) {

        String sessionReferer = (String) session.getAttribute("referer");

        if (sessionReferer != null && sessionReferer.equals("list")) {
            if (beforeURL != null && beforeURL.endsWith("list")) {
                memberService.setReadCountUpdate(num);
            }
            session.removeAttribute("referer");
        }

        Member member = memberService.getDetail(num);

        if (member == null) {
            mv.setViewName("error/error");
            mv.addObject("url",request.getRequestURL());
            mv.addObject("message","상세보기 실패");
        }else {
            mv.setViewName("user/userNoticeDetail");
            mv.addObject("noticedata", member);
        }
        return "user/userNotice";
    }

}

