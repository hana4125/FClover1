package hello.fclover.controller;


import hello.fclover.service.GoodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


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
   /* @PostMapping("/SearchGoodsProcess")
    public List<Goods> searchGoodsProcess(@ModelAttribute Goods goods) {
        List<Goods> list = new ArrayList<>();
        return list;
    }*/
}
