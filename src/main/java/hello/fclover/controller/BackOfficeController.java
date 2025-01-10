package hello.fclover.controller;


import hello.fclover.domain.Delivery;
import hello.fclover.domain.Payment;
import hello.fclover.domain.Seller;
import hello.fclover.service.BackOfficeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


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
    public List<Delivery> deliveryReadyAsync(ModelAndView mv) {
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
    @GetMapping("/delivery/ready")
    @ResponseBody
    public  void  ready(@RequestParam Long orderId,@RequestParam String userId,Model model) {
        System.out.println("test1");
        //조회한 주문정보 기반으로 delivery테이블에 insert하기.
        backOfficeService.InsertdeliveryReadyList(orderId,userId);

    }








}
