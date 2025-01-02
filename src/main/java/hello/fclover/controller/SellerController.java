package hello.fclover.controller;


import hello.fclover.service.SellerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Slf4j
@Controller
@RequestMapping(value="/seller")
public class SellerController {

    private final SellerService sellerService;

    SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }


    @GetMapping("/main")
    public String signup() {
        return "seller/sellerMypage";
    }

}
