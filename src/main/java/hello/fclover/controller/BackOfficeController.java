package hello.fclover.controller;


import hello.fclover.domain.*;
import hello.fclover.dto.PaymentDeliveryDTO;
import hello.fclover.service.BackOfficeService;
import hello.fclover.service.MemberService;
import hello.fclover.service.SellerService;
import hello.fclover.task.SettlementScheduledTasks;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Map;


@Slf4j
@Controller
@RequestMapping(value="/bo")
@RequiredArgsConstructor
public class BackOfficeController {

    private final BackOfficeService backOfficeService;
    private final MemberService memberService;
    private final SellerService sellerService;
    private final PasswordEncoder passwordEncoder;
    private static final int SIGNUP_SUCCESS = 1;
    private static final int SIGNUP_FAILURE = 0;
    private final SettlementScheduledTasks settlementScheduledTasks;

    @ModelAttribute("admin")
    public Member addAdminToModel(Principal principal) {

        if (principal != null) {
            String memberId = principal.getName();
            return memberService.findMemberById(memberId);
        }
        return null;
    }

    @GetMapping("/main")
    public String main1() {
        return "backOffice/boMain";
    }

    @GetMapping("/login")
    public String login() {
        return "backOffice/boLogin";
    }

    @GetMapping("/signup")
    public String signupForm() {
        return "backOffice/boSignup";
    }

    @PostMapping("/signupProcess")
    public String signup(@ModelAttribute("signupMember") Member member, RedirectAttributes redirectAttributes) {
        String memberIdDuplicate = memberService.isMemberIdDuplicate(member.getMemberId());
        String sellerIdDuplicate = sellerService.isSellerIdDuplicate(member.getMemberId());

        if (memberIdDuplicate != null || sellerIdDuplicate != null) {
            redirectAttributes.addFlashAttribute("message", "사용중인 아이디입니다.");
            return "redirect:/bo/signup";
        }

        String encPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encPassword);
        int result = memberService.signup(member);

        if (result == SIGNUP_SUCCESS) {
            log.info("회원가입 완료");
            return "redirect:/bo/login";
        } else {
            throw new RuntimeException("회원가입 실패");
        }
    }

    //주문완료
    @GetMapping("/deliveryOrder")
    public String deliver() {
        return "backOffice/boDeliveryOrder";
    }

    //배송준비중
    @GetMapping("/deliveryReady")
    public String deliveryReady() {
        return "backOffice/boDeliveryReady";
    }

    //배송준비중 데이터 비동기처리
    @GetMapping("/deliveryReadyAsync")
    @ResponseBody
    public List<PaymentDeliveryDTO> deliveryReadyAsync() {
        List<PaymentDeliveryDTO> list = backOfficeService.deliveryReadyOrderSearch();
        log.info("=======>여기는 controller(/deliveryReadyAsync) : " + list);
        return list;
    }

    //배송중
    @GetMapping("/deliveryInTransit")
    public String deliveryInTransit() {
        return "backOffice/boDeliveryInTransit";
    }

    //배송완료
    @GetMapping("/deliveryDone")
    public String deliveDone() {
        return "backOffice/boDeliveryDone";
    }

    //판매자 정산관리
    @GetMapping("/SellerSettlement")
    public String SellerSettlement() {

        return "backOffice/boSellerSettlement";
    }


    //판매자 정보 조회 페이지 이동
    @GetMapping("/info")
    public String sellerinfo() {
        return "backOffice/boSellerInfo";
    }

    //판매자 정보 조회 데이터
    @GetMapping("/infoSearch")
    @ResponseBody
    public List<Seller> sellerinfoSearch(Model model) {
        List<Seller> sellers = backOfficeService.searchSeller();
        System.out.println("=======>여기는 컨트롤러 : sellers = " + sellers);
        return sellers;
    }

    @GetMapping("/pendingCheck")
    @ResponseBody
    public List<Seller> sellerPendingCheck() {
        log.info("셀러 팬딩 체크" + backOfficeService.sellerPendingCheck().toString());
        return backOfficeService.sellerPendingCheck();
    }

    @PostMapping("/pendingApproved")
    @ResponseBody
    public void sellerPendingApproved(@RequestBody Map<String, String> data) {
        String sellerNo = data.get("sellerNo");

        try {
            backOfficeService.updateSellerApproved(Long.parseLong(sellerNo));
        } catch (NumberFormatException e) {
            log.info("숫자로 변환할 수 없습니다.");
        }

    }

    @PostMapping("/pendingRejected")
    @ResponseBody
    public void sellerPendingRejected(@RequestBody Map<String, String> data) {
        String sellerNo = data.get("sellerNo");
        try {
            backOfficeService.updateSellerRejected(Long.parseLong(sellerNo));
        } catch (NumberFormatException e) {
            log.info("숫자로 변환할 수 없습니다.");
        }
    }

    //결제정보 조회 데이터
    @GetMapping("/delivery/SearchOrder")
    @ResponseBody
    public  List<Payment> SearchOrder() {
        List<Payment> payment = backOfficeService.searchOrder();
        System.out.println("=======>여기는 컨트롤러 payment = " + payment);
        return payment;
    }

    //결제완료페이지에서 [배송준비] 버튼 클릭 시
    @GetMapping("/delivery/readyClick")
    @ResponseBody
    public  void  ready(@RequestParam Long paymentsNo,@RequestParam String userId,Model model) {
        log.info("============controller /delivery/ready : paymentsNo = " + paymentsNo + ", userId = " + userId);
        backOfficeService.InsertdeliveryReadyList(paymentsNo,userId);

    }

    //운송장번호 등록
    @GetMapping("/delivery/trackingNumberClick")
    @ResponseBody
    public ResponseEntity<?> trackingNumberSubmit(@RequestParam Long deliNo,@RequestParam Long paymentsNo,Model model) {

        System.out.println("================>여기는 트래킹넘버 컨트롤러 ");

//        String deliNo = requestBody.get("deliNo").toString();
//        String deliNum = requestBody.get("deliveryNum").toString();
        String deliCompany = "CJ대한통운";
        String deliStatus = "배송중";


        // 트래킹 넘버 정보를 서비스에 전달 (insertTrackingNumber 메서드에 전달)
        try {
            backOfficeService.insertTrackingNumber(deliNo, paymentsNo, deliCompany, deliStatus);
            return ResponseEntity.ok().body("운송장 정보가 성공적으로 등록되었습니다.");
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("잘못된 숫자 형식입니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }


    }

    //배송중 상태인 데이터 불러오기 deliveryInTransitAsync
    @GetMapping("/deliveryInTransitAsync")
    @ResponseBody
    public List<Delivery> deliveryInTransitAsync() {
        List<Delivery> inTransitList = backOfficeService.deliveryInTransitOrderSearch();

        System.out.println("===========>여기는 컨트롤러 inTransitList = " + inTransitList);
        return inTransitList;
    }

    //배송중 페이지에서 [배송완료] 버튼 눌렀을 시
    @GetMapping("/delivery/DoneClick")
    @ResponseBody
    public ResponseEntity<?> deliveryInTransitClick(@RequestParam int deliNo) {
        backOfficeService.changeDeliveryDoneStatus(deliNo);
        return ResponseEntity.ok().body("배송완료상태로 변경완료되었습니다.");


    }


    //배송완료 상태인 데이터 불러오기 deliveryDoneAsync
    @GetMapping("/deliveryDoneAsync")
    @ResponseBody
    public List<Delivery> deliveryDoneAsync() {
        List<Delivery> inTransitList = backOfficeService.deliveryDoneOrderSearch();

        System.out.println("===========>여기는 컨트롤러 inTransitList = " + inTransitList);
        return inTransitList;
    }

    //판매자 상품등록 승인
    @GetMapping("/goodsApproval")
    public String goodsApproval() {


        return "backOffice/boSellerGoodsApproval";
    }

    //판매자 정산 데이터 조회
    @GetMapping("/sellerSettlementSearch")
    @ResponseBody
    public List<Settlement> sellerSettlementSearch() {

        List<Settlement> list = backOfficeService.sellerSettlementSearch();
        return list;
    }

    //판매자 정산 데이터 조회
    @GetMapping("/SellerGoodsApproval")
    @ResponseBody
    public List<Goods> SellerGoodsApproval() {

        List<Goods> list = backOfficeService.sellerGoodsApprovalSearch();
        return list;
    }


    //판매자상품등록 [승인완료]버튼 클릭 시
    @GetMapping("/goodsConfirmSuccess")
    @ResponseBody
    public ResponseEntity<?> goodsConfirmSuccess(@RequestParam Long goodsNo) {

        backOfficeService.goodsConfirmSuccess(goodsNo);

        return ResponseEntity.ok().body("상품등록 승인완료되었습니다.");
    }



    //판매자 가입 상태
    @GetMapping("/sellerApproval")
    public String sellerApproval() {


        return "backOffice/boSellerPending";
    }
}
