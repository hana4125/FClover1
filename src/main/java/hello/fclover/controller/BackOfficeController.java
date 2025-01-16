package hello.fclover.controller;


import hello.fclover.domain.Delivery;
import hello.fclover.domain.Payment;
import hello.fclover.domain.Seller;
import hello.fclover.service.BackOfficeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;


@Slf4j
@Controller
@RequestMapping(value="/bo")
@RequiredArgsConstructor
public class BackOfficeController {

    private final BackOfficeService backOfficeService;

    @GetMapping("/main")
    public String main1() {
        return "backOffice/boMain";
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
    public List<Delivery> deliveryReadyAsync() {
        List<Delivery> list = backOfficeService.deliveryReadyOrderSearch();

        System.out.println("=======>여기는 controller list = " + list);
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
    public  void  ready(@RequestParam Long orderId,@RequestParam String userId,Model model) {
        log.info("============controller /delivery/ready : orderId = " + orderId + ", userId = " + userId);
        backOfficeService.InsertdeliveryReadyList(orderId,userId);

    }


    //운송장번호 등록
    @PostMapping("/delivery/trackingNumberClick")
    @ResponseBody
    public ResponseEntity<?> trackingNumberSubmit(@RequestBody Map<String,Object> requestBody) {

        System.out.println("================>여기는 트래킹넘버 컨트롤러 ");

        String deliNo = requestBody.get("deliNo").toString();
        String deliNum = requestBody.get("deliveryNum").toString();
        String deliCompany = "CJ대한통운";
        String deliStatus = "배송중";


        backOfficeService.insertTrackingNumber(Integer.valueOf(deliNo),Integer.valueOf(deliNum),deliCompany,deliStatus);


        return ResponseEntity.ok().body("운송장 정보가 성공적으로 등록되었습니다.");

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
    public ResponseEntity<?> deliveryInTransitClick(@RequestBody int deliNo) {
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






}
