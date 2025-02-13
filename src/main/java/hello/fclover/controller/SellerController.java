package hello.fclover.controller;


import hello.fclover.domain.*;
import hello.fclover.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/seller")
public class SellerController {

    private final MemberService memberService;
    private final SellerService sellerService;
    private final GoodsService goodsService;
    private final CartService cartService;
    private final PasswordEncoder passwordEncoder;
    private final CategoryService categoryService;
    private final BackOfficeService backOfficeService;

    @ModelAttribute("seller")
    public Seller addSellerToModel(Principal principal) {

        if (principal != null) {
            String sellerId = principal.getName();
            return sellerService.findSellerById(sellerId);
        }
        return null;
    }

    @GetMapping("/addSingleProduct")
    public String addSingleProduct(Model model, Goods goods) {
        List<Category> categoryList = categoryService.getCategoryList();
        model.addAttribute("categoryList", categoryList);
        return "seller/sellerAddSingleProduct";
    }

    //단일 상품등록 프로세스
    @PostMapping(value = "/addSingleProductProcess", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public String addSingleProductProcess(@RequestPart("goods") Goods goods,
                                          @RequestPart("goodsImages") List<MultipartFile> images,
                                          Principal principal) throws IOException {
        System.out.println("컨트롤단");
        for (MultipartFile file : images) {
            System.out.println("goods:" + file.getOriginalFilename());
        }

        String sellerId = principal.getName();
        Seller seller = sellerService.findSellerById(sellerId);
        Long sellerNo = seller.getSellerNo();
        goods.setSellerNo(sellerNo);
        String businessNumber = seller.getBusinessNumber();

        System.out.println("goods:" + goods);

        goodsService.goodsSingleInsert(goods, images, businessNumber);
        return "redirect:/seller/main";
    }

    @GetMapping("/productDetail")
    public String productDetail(Model model, Principal principal) {
        List<Category> categoryList = categoryService.getCategoryList();
        model.addAttribute("categoryList", categoryList);

        return "seller/sellerProductDetail";
    }

    @GetMapping("/main")
    public String signup(Principal principal) {

        if (principal == null) {
            return "redirect:/seller/login";
        }

        return "seller/sellerMain";
    }


    @GetMapping("/signup")
    public String sellerSignupForm() {
        return "seller/sellerSignup";
    }

    @PostMapping("/signupProcess")
    public String sellerSignup(HttpServletRequest request, RedirectAttributes redirectAttributes) {

        String sellerId = request.getParameter("sellerId");

        String memberIdDuplicate = memberService.isMemberIdDuplicate(sellerId);
        String sellerIdDuplicate = sellerService.isSellerIdDuplicate(sellerId);

        if (memberIdDuplicate != null || sellerIdDuplicate != null) {
            redirectAttributes.addFlashAttribute("message", "사용중인 아이디입니다.");
            return "redirect:/seller/signup";
        }

        //이유는 모르겠지만 ModelAttribute가 안됨
        Seller seller = new Seller();
        seller.setSellerId(sellerId);
        seller.setPassword(passwordEncoder.encode(request.getParameter("password")));
        seller.setName(request.getParameter("name"));
        seller.setEmail(request.getParameter("email"));
        seller.setPhoneNumber(request.getParameter("phoneNumber"));
        seller.setBusinessNumber(request.getParameter("businessNumber"));
        seller.setCompanyName(request.getParameter("companyName"));

        sellerService.signup(seller);
        return "redirect:/seller/main";
    }

    @GetMapping("/login")
    public String sellerLoginForm(HttpSession session, Model model) {

        model.addAttribute("message", session.getAttribute("sellerLoginfail"));
        session.removeAttribute("sellerLoginfail");

        return "seller/sellerLogin";
    }

    //판매자 일정산 페이지
    @GetMapping("/sellerDaySettlement")
    public String sellerDaySettlement(Principal principal, Model model) {
        System.out.println("principal = " + principal.getName());
        List<Settlement> daySettlement = sellerService.searchDaySettlement(principal.getName());
        model.addAttribute("daySettlement", daySettlement);

        System.out.println("daySettlement = " + daySettlement);

        return "seller/sellerDaySettlement";
    }

    //판매자 월정산 페이지
    @GetMapping("/sellerMonthSettlement")
    public String sellerMonthSettlement() {
        return "seller/sellerMonthSettlement";
    }

    @ResponseBody
    @PostMapping("/pendingCheck")
    public Seller pendingCheck(@RequestBody Map<String, String> data) {
        String sellerId = data.get("sellerId");
        String password = data.get("password");

        Seller seller = sellerService.findSellerById(sellerId);

        if (seller == null) {
            return null;
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (!passwordEncoder.matches(password, seller.getPassword())) {
            return null;
        }

        log.info(seller.toString());
        return seller;
    }

    @PostMapping("/SearchGoodsProcess")
    @ResponseBody
    public ResponseEntity<?> searchGoodsProcess(@RequestParam Map<String, String> params,
                                                Principal principal) {
        System.out.println("params = " + params);

        params.get("cateNo");
        List<Goods> goodsList = goodsService.sellerGoodsSearch(params);
        System.out.println("goodsList = " + goodsList);
        return ResponseEntity.ok(goodsList);
    }


    @GetMapping("/sellerAddMassProduct")
    public String addMassProduct(Model model, Principal principal) {
        List<Category> categoryList = categoryService.getCategoryList();
        model.addAttribute("categoryList", categoryList);
        return "seller/sellerAddMassProduct";
    }

    //구매자 리스트 조회
    @GetMapping(value = "/buyerList")
    public ModelAndView getBuyerList(
            Principal principal,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "3") int limit,
            @RequestParam(defaultValue = "") String searchWord,
            @RequestParam(defaultValue = "") String searchField,
            HttpServletRequest request) {

        ModelAndView mnv = new ModelAndView();
        String sellerId = principal.getName();
        Long sellerNo = sellerService.getselectNo(sellerId);

        // 전체 구매자 주문 목록 개수 가져오기
        int totalcount = sellerService.getListCount(searchWord, sellerNo, searchField);
        // 현재 페이지에 해당하는 구매자 리스트 가져오기
        List<Map<String, Object>> orderList = sellerService.getListDetail(page, searchWord, limit, sellerNo, searchField);

        if (totalcount == 0 && page > 1) {
            page = 1;
        }
        PaginationResult result = new PaginationResult(page, limit, totalcount);

        if (orderList.isEmpty()) {
            mnv.addObject("message", "구매자 주문 정보가 없습니다.");
        }

        mnv.setViewName("seller/sellerBuyerList");
        mnv.addObject("orderList", orderList);
        mnv.addObject("searchWord", searchWord);
        mnv.addObject("searchField", searchField);
        mnv.addObject("searchlistcount", totalcount);
        mnv.addObject("startpage", result.getStartpage());
        mnv.addObject("endpage", result.getEndpage());
        mnv.addObject("maxpage", result.getMaxpage());
        mnv.addObject("totalcount", totalcount);
        mnv.addObject("limit", limit);
        mnv.addObject("page", page);
        return mnv;
    }

    @PostMapping("deleteGoodsProcess")
    @ResponseBody
    public ResponseEntity<List<String>> deleteGoodsProcess(@RequestParam("deleteGoodsNo") Long goodsNo) {
        List<Goods> goodsList = goodsService.deleteGoods(goodsNo);

        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping("getGoodsUpdateDetail")
    public ResponseEntity<Goods> getGoodsDetail(@RequestParam("goodsNo") Long goodsNo) {
        Goods goods = goodsService.getGoodsUpdateFormDetail(goodsNo);
        System.out.println("goods데이터 = " + goods);
        if (goods != null) {
            return ResponseEntity.ok(goods);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/updateGoodsProcess")
    public ResponseEntity<String> updateGoods(Goods goods) {
        System.out.println("goods = " + goods);
        int result = goodsService.updateGoods(goods);
        if (result > 0) {
            return ResponseEntity.ok("상품 업데이트 성공");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}