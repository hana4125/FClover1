package hello.fclover.controller;


import hello.fclover.domain.Payment;
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


    @GetMapping("/info")
    public String sellerinfo(Model model) {
        return "backOffice/boSellerInfo";
    }


    @GetMapping("/delivery/SearchOrder")
    @ResponseBody
    public  List<Payment> SearchOrder() {
        List<Payment> payment = backOfficeService.searchOrder();
        System.out.println("=======>여기는 컨트롤러 payment = " + payment);
        return payment;
    }

    //재고 감소
    @GetMapping("/StockCount")
    public String StockCount(Model model) {
        int goods_no = 3;
        int stockCount = backOfficeService.getProduct_stock(goods_no);
        int result =  backOfficeService.decrease(stockCount);



        return "backOffice/boSellerSettlement";
    }



}
