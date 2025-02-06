package hello.fclover.controller;


import hello.fclover.domain.Category;
import hello.fclover.domain.Goods;
import hello.fclover.domain.PaginationResult;
import hello.fclover.domain.Seller;
import hello.fclover.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.security.Principal;
import java.io.IOException;
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
        return "sellerAddSingleProduct";
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

        return "sellerProductDetail";
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
    public String sellerDaySettlement() {
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

    //구매자 리스트 조회
    @GetMapping(value = "/buyerList")
    public ModelAndView getBuyerList(
            @RequestParam(value = "n", defaultValue = "1") int n,
            @RequestParam(value = "search_word", required = false) String searchWord,
            HttpServletRequest request) {

        ModelAndView mnv = new ModelAndView();
        List<Map<String, Object>> orderList = sellerService.getListDetail(n, searchWord);

        // 페이징 관련 변수 계산
        int totalCount = orderList.size(); // 전체 데이터 수
        int pageSize = 10; // 한 페이지당 보여줄 게시물 수
        int blockSize = 5; // 페이지 블록 크기

        // 시작페이지와 끝페이지 계산
        int startPage = ((n - 1) / blockSize) * blockSize + 1;
        int endPage = Math.min(startPage + blockSize - 1, (totalCount + pageSize - 1) / pageSize);

        if (orderList == null) {
            mnv.setViewName("error/error");
            mnv.addObject("url", request.getRequestURL());
            mnv.addObject("message", "구매자 주문 정보를 찾을 수 없습니다.");
        } else {
            mnv.setViewName("seller/sellerBuyerList");
            mnv.addObject("orderList", orderList);
            mnv.addObject("search_word", searchWord);

            // 페이징 관련 변수 전달
            mnv.addObject("startpage", startPage);
            mnv.addObject("endpage", endPage);
            mnv.addObject("totalCount", totalCount);
            mnv.addObject("pageSize", pageSize);
            mnv.addObject("n", n); // 현재 페이지 번호
        }
        return mnv;
    }

    //구매자 검색
    @GetMapping(value = "/buyerSearch")
    public ModelAndView getBuyerSearch(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "30") int limit,
            ModelAndView mv,
            @RequestParam(defaultValue = "") String search){

        int searchlistcount =  sellerService.getSearchListCount(search);
        List<Seller> buyList = sellerService.getSearchList(search,page,limit);
        PaginationResult result = new PaginationResult(page,limit,searchlistcount);

        mv.setViewName("seller/sellerBuyerList");
        mv.addObject("page",page);
        mv.addObject("maxpage",result.getMaxpage());
        mv.addObject("startpage",result.getStartpage());
        mv.addObject("endpage",result.getEndpage());
        mv.addObject("buyList", buyList);
        mv.addObject("limit",limit);
        mv.addObject("searchlistcount",searchlistcount);
        return mv;
    }

}

