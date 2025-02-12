package hello.fclover.controller;

import hello.fclover.domain.*;
import hello.fclover.dto.CartDTO;
import hello.fclover.dto.PaymentGoodsDTO;
import hello.fclover.dto.WishDTO;
import hello.fclover.mail.EmailMessage;
import hello.fclover.mail.EmailService;
import hello.fclover.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.HttpURLConnection;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping(value = "/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final SellerService sellerService;
    private final PasswordEncoder passwordEncoder;
    private final PaymentService paymentService;
    private final GoodsService goodsService;
    private final WishService wishService;
    private final EmailService emailService;
    private final NoticeService noticeService;
    private static final int SIGNUP_SUCCESS = 1;
    private static final int SIGNUP_FAILURE = 0;

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
    public String signupProcess(@ModelAttribute("signupMember") Member member, RedirectAttributes redirectAttributes) {

        String memberIdDuplicate = memberService.isMemberIdDuplicate(member.getMemberId());
        String sellerIdDuplicate = sellerService.isSellerIdDuplicate(member.getMemberId());

        if (memberIdDuplicate != null || sellerIdDuplicate != null) {
            redirectAttributes.addFlashAttribute("message", "사용중인 아이디입니다.");
            return "redirect:/member/signup";
        }

        String encPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encPassword);
        int result = memberService.signup(member);

        if (result == SIGNUP_SUCCESS) {
            memberService.createCoupon(member.getMemberId());
            log.info("회원가입 완료");
            return "redirect:/";
        } else {
            throw new RuntimeException("회원가입 실패");
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

    @GetMapping("/find-id")
    public String findIdPage() {
        return "user/userFindId";
    }

    @ResponseBody
    @PostMapping("/find-id")
    public String findId(@RequestBody Map<String, String> formData) {

        String name = formData.get("name");
        String birthdate = formData.get("birthdate");
        String email = formData.get("email");

        Member member = new Member();

        member.setName(name);
        member.setBirthdate(birthdate);
        member.setEmail(email);

        return memberService.findMemberId(member);
    }

    @ResponseBody
    @PostMapping("/send-code-id")
    public String sendCodeId(@RequestBody String email) {
        String certCode = EmailService.generateRandomNumber();
        EmailMessage emailMessage = EmailMessage.builder()
                .to(email)
                .subject("[네잎클로버] 아이디 찾기를 위한 인증 메일이에요.")
                .message("인증번호 : " + certCode)
                .build();

        emailService.asyncSendMail(emailMessage);
        return certCode;
    }


    @GetMapping("/find-id-ok")
    public String findOkPage(@RequestParam String name, @RequestParam String birthdate, @RequestParam String email, Model model) {
        Member member = new Member();
        member.setName(name);
        member.setBirthdate(birthdate);
        member.setEmail(email);

        String memberId = memberService.findMemberId(member);
        Member findMember = memberService.findMemberById(memberId);
        model.addAttribute("member", findMember);

        return "user/userFindIdOk";
    }

    @GetMapping("/reset-password")
    public String resetPasswordPage() {
        return "user/userResetPassword";
    }

    @ResponseBody
    @PostMapping("/reset-password")
    public Member resetPassword(@RequestBody Map<String, String> formData) {
        String memberId = formData.get("memberId");
        String name = formData.get("name");
        String birthdate = formData.get("birthdate");
        String email = formData.get("email");

        Member member = new Member();

        member.setMemberId(memberId);
        ;
        member.setName(name);
        member.setBirthdate(birthdate);
        member.setEmail(email);

        return memberService.selectMemberResetPassword(member);
    }

    @GetMapping("/reset-password-ok")
    public String resetPasswordOkPage(@RequestParam String memberId, Model model) {
        Member member = memberService.findMemberById(memberId);
        model.addAttribute("member", member);
        return "user/userResetPasswordOk";
    }

    @PostMapping("/reset-password-ok")
    public String resetPasswordOkProcess(@RequestParam String memberId, @RequestParam String newPassword, RedirectAttributes redirectAttributes) {
        Member member = memberService.findMemberById(memberId);
        member.setPassword(passwordEncoder.encode(newPassword));
        memberService.updateMember(member);
        redirectAttributes.addFlashAttribute("message", "비밀번호가 변경되었습니다.");
        return "redirect:/member/main";
    }

    @ResponseBody
    @PostMapping("/send-code-password")
    public String sendCodePassword(@RequestBody String email) {
        String certCode = EmailService.generateRandomNumber();
        EmailMessage emailMessage = EmailMessage.builder()
                .to(email)
                .subject("[네잎클로버] 비밀번호 찾기를 위한 인증 메일이에요.")
                .message("인증번호 : " + certCode)
                .build();
        emailService.asyncSendMail(emailMessage);
        return certCode;
    }

    @GetMapping("/myPage")
    public String myPageMain() {

        return "user/mypage/userMyPageMain";
    }

    @GetMapping("/myPage/addressBook")
    public String myPageDeliveryAddressBook(Principal principal, Model model) {
        String memberId = principal.getName();
        Member member = memberService.findMemberById(memberId);
        Long memberNo = member.getMemberNo();

        AddressBook defaultAddress = memberService.getDefaultAddress(memberNo);
        List<AddressBook> addressBookList = memberService.getDeliveryAddress(memberNo);

        model.addAttribute("defaultAddress", defaultAddress);
        model.addAttribute("addressBookList", addressBookList);

        return "user/mypage/userMyPageAddressBook";
    }

    @PostMapping("/addAddressBook")
    public String addDeliveryAddress(@ModelAttribute AddressBook addressBook, Principal principal) {
        String memberId = principal.getName();
        long memberNo = memberService.getmemberNo(memberId);
        addressBook.setMemberNo(memberNo);
        memberService.addDeliveryAddress(addressBook);
        return "redirect:/member/myPage/addressBook";
    }

    @GetMapping("/deleteAddressBook")
    public String deleteDeliveryAddress(@RequestParam int addressNo, RedirectAttributes redirectAttributes) {
        int result = memberService.checkDefaultAddress(addressNo);

        if (result == 1) {
            redirectAttributes.addFlashAttribute("message", "기본 배송지는 삭제하실수 없습니다.");
        } else {
            memberService.removeAddressBook(addressNo);
        }
        return "redirect:/member/myPage/addressBook";
    }


    @Transactional
    @PostMapping("/defaultAddress")
    public String defaultAddress(@RequestParam int addressNo) {
        memberService.setDefaultAddress(addressNo);
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

        return "user/mypage/userMyPageInfo";
    }

    @GetMapping("/myPage/info/modify")
    public String memberUpdateForm() {

        return "user/mypage/userMyPageInfoUpdateForm";
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

        return "user/mypage/userMyPageProfile";
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

//    @GetMapping("/myPage/purchaseHistory")
//    public String myPagePurchaseHistory() {
//        return "/user/mypage/userMyPagePurchaseHistory";
//    }

    @GetMapping("/myPage/wishlist")
    public String myPageWishlist(Principal principal, Model model) {
        String memberId = principal.getName();
        Long memberNo = memberService.getmemberNo(memberId);
        List<WishDTO> wishlist = memberService.getWishDTOList(memberNo);

        for (WishDTO wishDTO : wishlist) {
            GoodsImage mainImage = goodsService.getMainImageByGoodsNo(wishDTO.getGoodsNo());
            wishDTO.setMainImage(mainImage);
        }

        model.addAttribute("wishlist", wishlist);
        return "user/mypage/userMyPageWishlist";
    }

    @PostMapping("/myPage/wishlist/delete")
    public ResponseEntity<String> deleteWishList(Principal principal, @RequestBody Map<String, String> data) {
        String memberId = principal.getName();
        Long memberNo = memberService.getmemberNo(memberId);
        String wishNo = data.get("wishNo");

        try {
            memberService.removeWishList(Long.parseLong(wishNo), memberNo);
        } catch (NumberFormatException e) {
            log.info(e.getMessage());
        }

        return ResponseEntity.ok(data + " 삭제 완료");
    }

    @PostMapping("/myPage/wishlist/deleteAll")
    public ResponseEntity<String> deleteAllWishList(@RequestBody Map<String, String> data) {

        String memberNo = data.get("memberNo");

        try {
            memberService.removeAllWishList(Long.parseLong(memberNo));
        } catch (NumberFormatException e) {
            log.info("숫자로 변환할 수 없습니다.");
        }

        return ResponseEntity.ok("전체 삭제 완료");
    }

    @GetMapping("/cart")
    public String cart(Principal principal, Model model) {
        String memberId = principal.getName();
        Long memberNo = memberService.getmemberNo(memberId);

        List<CartDTO> cartItems = memberService.getCartItems(memberNo);

        for (CartDTO cartItem : cartItems) {
            GoodsImage mainImage = goodsService.getMainImageByGoodsNo(cartItem.getGoodsNo());
            cartItem.setMainImage(mainImage); // 이미지 불러오기
            if (cartItem.getDeliveryDate() == null && cartItem.getCreatedAt() != null) {
                LocalDateTime deliveryDate = cartItem.getCreatedAt().plusDays(3);
                cartItem.setDeliveryDate(deliveryDate);
            }
        }

        model.addAttribute("cartItems", cartItems);

        return "user/userCart";
    }

    @PostMapping("/cart/delete")
    public ResponseEntity<String> deleteCart(@RequestBody Map<String, String> data) {

        String cartNo = data.get("cartNo");
        try {
            memberService.removeCartItems(Long.parseLong(cartNo));
        } catch (NumberFormatException e) {
            log.info(e.getMessage());
        }
        return ResponseEntity.ok("삭제된 상품 cartNo : " + cartNo);
    }

/*    @GetMapping("/steadySeller")
    public String steadySeller() {
        return "user/userSteadyseller";
    }*/

/*    @GetMapping("/newItems")
    public String newItems() {
        return "user/userNewItems";
    }*/
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
        memberService.updateSocialMember(member);
        return "redirect:/member/myPage/info";
    }

    @GetMapping("/memberPay")
    public String sellerPay(@RequestParam String goodsName,@RequestParam int goodsPrice,@RequestParam String goodsWriter,@RequestParam int quantity, @RequestParam Long goodsNo, Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }

        Long memberNo = memberService.getmemberNo(principal.getName());
        AddressBook defaultAddress =memberService.getDefaultAddress(memberNo);
        model.addAttribute("defaultAddress", defaultAddress);

        goodsService.getGoodsDetail(goodsNo, model);
        model.getAttribute("imageList");

        model.addAttribute("username", principal.getName());
        model.addAttribute("goodsName", goodsName);
        model.addAttribute("goodsPrice", goodsPrice);
        model.addAttribute("goodsWriter", goodsWriter);
        model.addAttribute("quantity", quantity);
        model.addAttribute("goodsNo", goodsNo);


        List<Coupon> coupons = memberService.getActiveCouponsForUser(principal.getName());
        model.addAttribute("coupons", coupons);



        return "user/userPayments";
    }



    @GetMapping("/memberPayDone")
    public String sellerPayDone() {
        return "user/userPaymentsDone";
    }


    //마이페이지 주문/배송 조회
    @GetMapping("/myPage/orderDelivery")
    public String myPageOrderDelivery(Principal principal, Model model) {
        List<PaymentGoodsDTO> payment = paymentService.searchList(principal.getName());

        model.addAttribute("list", payment);
        //상품 이미지에 대한 값 넘겨주어야 함. goods_image테이블.

        return "user/mypage/userOrderlist";
    }


    //마이페이지 주문/배송조회 상세보기
    @GetMapping("/myPage/memberOrderListDetail")
    public String OrderListDetail(@RequestParam("orderId") Long orderId, Model model,Principal principal) {
        PaymentGoodsDTO payment = paymentService.searchOneOrderDetail(principal.getName(),orderId);

        model.addAttribute("list", payment);
        model.addAttribute("orderId", orderId);

        return "user/mypage/userOrderlistDetail";
    }

    //포트원 결제
    @PostMapping("/portone")
    public ResponseEntity<Map<String, String>> savePortone(@RequestBody PaymentReq paymentRequest) {
        Map<String, String> response = new HashMap<>();
        System.out.println("========>controller의 paymentRequest : " + paymentRequest);

        try {
            HttpURLConnection connection = paymentService.createConnection("https://service.iamport.kr/payments/ready/imp03578475/nice/iamport00m?sandbox=true&store_id=store-a0b049dc-4590-4213-b1f5-d861a3ccae51&channelKey=channel-key-68c69d42-0462-4eb9-af59-5b26cb4100de");

            if (paymentService.isConnectionSuccessful(connection)) {
                // 결제 처리
                paymentService.savePayment(Payment.save(paymentRequest));

                response.put("message", "Payment processed successfully.");
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "Failed to connect to payment service.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Failed to process payment.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/OrderCancel/{uid}")
    public ResponseEntity<String> OrderCancel(@PathVariable("uid") String uid) {
        paymentService.cancelPayment(uid);

        return ResponseEntity.ok("Payment cancel processed successfully.");
    }

//    @GetMapping("/goodsDetail/{no}")
//    public String goodsDetail(@PathVariable("no") Long goodsNo, @ModelAttribute("member") Member member, Model model) {
//        // 카테고리 데이터 가져오기
//        Goods goods = goodsService.findGoodsByNo(goodsNo);
//
//        model.addAttribute("goods", goods);
//        return "/user/userGoodsDetail";
//    }

    @GetMapping("/gift")
    public String gift() {
        return "user/userGoodsDetail";
    }


    //내쿠폰 조회페이지
    @GetMapping("/myPage/coupon")
    public String coupon(Principal principal,Model model) {

        String memberId  = principal.getName();
        List<Coupon> coupons = memberService.getActiveCouponsForUser(memberId);
        log.info("========> memberController의 coupons 조회 : " + coupons);

        model.addAttribute("coupons", coupons);

        return "user/mypage/userMypageCoupon";
    }



}
