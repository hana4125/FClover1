package hello.fclover.controller;


import hello.fclover.service.BackOfficeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Slf4j
@Controller
@RequestMapping(value="/bo")
public class BackOfficeController {


    //아 또왜 롬복 에러나냐

    private final BackOfficeService backOfficeService;

    BackOfficeController(BackOfficeService backOfficeService) {
        this.backOfficeService = backOfficeService;
    }

    @GetMapping("/main")
    public String main1(Model model) {
        return "backOffice/boMain";
    }


    @GetMapping("/delivery")
    public String deliver(Model model) {
        return "backOffice/boDeliveryManagement";
    }


    @GetMapping("/info")
    public String sellerinfo(Model model) {
        return "backOffice/boSellerInfo";
    }

    @GetMapping("/settlement")
    public String test1(Model model) {
        return "backOffice/boSellerSettlement";
    }

}
