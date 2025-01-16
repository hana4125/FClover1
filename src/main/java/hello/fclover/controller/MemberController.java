package hello.fclover.controller;

import hello.fclover.domain.*;
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

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping(value="/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final SellerService sellerService;
    private final PasswordEncoder passwordEncoder;
    private final PaymentService paymentService;
    private final CategoryService categoryService;
    private final GoodsService goodsService;


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

    @PostMapping("/find-id")
    public ResponseEntity<String> findId(@RequestBody Map<String, String> formData) {

        String name = formData.get("name");
        String birthdate = formData.get("birthdate");
        String email = formData.get("email");



        return ResponseEntity.ok("아이디 찾기 성공");
    }

    @GetMapping("/find-id-ok")
    public String findOkPage(@RequestParam(required = false) String memberId, Model model) {
        if (memberId != null) {
            Member member = memberService.findMemberById(memberId);
            model.addAttribute("member", member);
        }
        return "user/userFindIdOk";
    }

    @GetMapping("/reset-password")
    public String resetPasswordPage() {
        return "user/userResetPassword";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@ModelAttribute("findMember") Member member, RedirectAttributes redirectAttributes) {
        Integer memberNum = memberService.selectMemberResetPassword(member);
        if (memberNum == null) {
            redirectAttributes.addFlashAttribute("message", "일치하는 회원 아이디가 없습니다.");
            return "redirect:/member/reset-password";
        }
        redirectAttributes.addFlashAttribute("message", "메일 발송 성공");

        String randomNumber = EmailService.generateRandomNumber();

        EmailMessage emailMessage = EmailMessage.builder()
                .to(member.getEmail())
                .subject("[네잎클로버] 비밀번호 찾기를 위한 인증 메일이에요.")
                .message("인증번호 : " + randomNumber)
                .build();
        emailService.sendMail(emailMessage);
        return "redirect:/member/reset-password";
    }

    @GetMapping("/myPage")
    public String myPageMain() {

        return "/user/mypage/userMyPageMain";
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
        int memberNo = memberService.getmemberNo(memberId);
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

//    @GetMapping("/myPage/purchaseHistory")
//    public String myPagePurchaseHistory() {
//        return "/user/mypage/userMyPagePurchaseHistory";
//    }

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
        memberService.updateSocialMember(member);
        return "redirect:/member/myPage/info";
    }

    @GetMapping("/memberPay")
    public String sellerPay(Principal principal,Model model) {
        if(principal==null){
            return "redirect:/login";
        }
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


    @GetMapping("/GoodsDetail")
    public String GoodsDetail() {
        System.out.println("====");
        return "/user/userGoodsDetail";
    }

    @GetMapping("/category/{no}")
    public String categoryDetail(@PathVariable("no") int cate_no,
                                 @RequestParam(value = "sort", required = false, defaultValue = "latest") String sort,
                                 @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                 @RequestParam(value = "size", required = false, defaultValue = "20") int size,
                                 Model model) {

        // 카테고리 데이터 가져오기
        List<Category> categoryList = categoryService.getCategoryList();
        model.addAttribute("categoryList", categoryList);
        // 상품 목록 가져오기
        List<Goods> goodsList = goodsService.getGoodsList(cate_no, sort, page, size);
        model.addAttribute("goodsList", goodsList);

        // 페이지네이션 정보 전달
        int totalItems = goodsService.getTotalGoodsCount(cate_no);
        int totalPages = (int) Math.ceil((double) totalItems / size);

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("sort", sort);
        model.addAttribute("size", size);
        return "/user/userCategory"; // 카테고리 상세 페이지
    }
}