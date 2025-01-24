package hello.fclover.controller;


import hello.fclover.domain.Goods;
import hello.fclover.service.GoodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/goods")
@RequiredArgsConstructor
public class GoodsController {
    private final GoodsService goodsService;

    @GetMapping("/GoodsDetail/{no}")
    public String goodsDetail(Model model, @PathVariable("no") Long goodsNo) {
        goodsService.getGoodsDetail(goodsNo, model);
        System.out.println("============================================");
        System.out.println("model = " + model.getAttribute("imageList"));
        return "user/userGoodsDetail";
    }
}
