package hello.fclover.controller;


import hello.fclover.domain.*;
import hello.fclover.mybatis.mapper.GoodsMapper;
import hello.fclover.mybatis.mapper.MessGoodsMapper;
import hello.fclover.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;



@Controller
@RequestMapping("/goods")
@RequiredArgsConstructor
public class GoodsController {
    private final GoodsService goodsService;

    private final GoodsMapper goodsMapper;
    private final MessGoodsMapper messGoodsMapper;

    private final CategoryService categoryService;
    private final MemberService memberService;
    private final WishService wishService;

    @ModelAttribute("member")
    public Member addMemberToModel(Principal principal) {

        if (principal != null) {
            String memberId = principal.getName();
            return memberService.findMemberById(memberId);
        }
        return null;
    }


    @GetMapping("/GoodsDetail/{no}")
    public String goodsDetail(Model model, @PathVariable("no") Long goodsNo) {
        goodsService.getGoodsDetail(goodsNo, model);
        System.out.println("model = " + model.getAttribute("imageList"));

        // 카테고리 데이터 가져오기
        List<Category> categoryList = categoryService.getCategoryList();
        model.addAttribute("categoryList", categoryList);

        return "user/userGoodsDetail";
    }
   /* @PostMapping("/SearchGoodsProcess")
    public List<Goods> searchGoodsProcess(@ModelAttribute Goods goods) {
        List<Goods> list = new ArrayList<>();
        return list;
    }*/

    @GetMapping("/category/{no}")
    public String categoryDetail(@PathVariable("no") int cateNo,
                                 @ModelAttribute("member") Member member,
                                 @RequestParam(value = "sort", required = false, defaultValue = "latest") String sort,
                                 @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                 @RequestParam(value = "size", required = false, defaultValue = "20") int size,
                                 Model model) {

        // 카테고리 데이터 가져오기
        List<Category> categoryList = categoryService.getCategoryList();
        model.addAttribute("categoryList", categoryList);

        // 현재 선택된 카테고리 객체 가져오기
        Category selectedCategory = categoryService.getCategoryByNo(cateNo);
        model.addAttribute("category", selectedCategory);

        // 회원 번호 가져오기
        Long memberNo = null;
        if (member != null) {
            memberNo = member.getMemberNo();
        }

        //상품 목록 조회
        List<Goods> goodsList = goodsService.getCategoryGoodsList(cateNo, sort, page, size);
        System.out.println("조회된 상품 수 : " + goodsList.size());
        model.addAttribute("goodsList", goodsList);

        // 찜 목록(회원이 찜한 상품 번호 목록) 조회
        if (memberNo != null) {
            List<Long> wishlist = wishService.getWishlistGoodsNos(memberNo);
            model.addAttribute("wishlist", wishlist);
        }

        // 대표 이미지 가져오기
        for (Goods goods : goodsList) {
            System.out.println("상품 번호 : " + goods.getGoodsNo());
            GoodsImage mainImage = goodsService.getMainImageByGoodsNo(goods.getGoodsNo());
            goods.setMainImage(mainImage);
        }

        // 페이지네이션 정보 전달
        int totalItems = goodsService.getTotalGoodsCount(cateNo);
        int totalPages = (int) Math.ceil((double) totalItems / size);

        int maxPageNumbersToShow = 10;
        int startPage;
        int endPage;

        if (totalPages <= maxPageNumbersToShow) {
            startPage = 1;
            endPage = totalPages;
        } else {
            if (page <= 6) {
                startPage = 1;
                endPage = 10;
            } else if (page + 4 >= totalPages) {
                startPage = totalPages - 9;
                endPage = totalPages;
            } else {
                startPage = page - 5;
                endPage = page + 4;
            }
        }

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("sort", sort);
        model.addAttribute("size", size);
        return "user/userCategory"; // 카테고리 상세 페이지
    }

    @GetMapping("/bestSeller")
    public String bestSeller(@ModelAttribute("member") Member member,
                             @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                             @RequestParam(value = "size", required = false, defaultValue = "20") int size,
                             Model model) {
        return prepareSellerPage(member, page, size, model, "user/userBestseller");
    }

    @PostMapping("/addMassProductProcess")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleFileUpload(@RequestParam("file") MultipartFile multipartFile) throws Exception {
        File file = getFile(multipartFile);

        ExcelSheetHandler excelSheetHandler = ExcelSheetHandler.readExcel(file);

        List<MessGoods> excelDatas = excelSheetHandler.getMessGoods();

        long beforeTime1 = System.currentTimeMillis();
        Map<String, Object> resultList = goodsService.saveMessproduct(excelDatas);
        long afterTime1 = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        long diffTime1 = afterTime1 - beforeTime1; // 두 개의 실행 시간
        System.out.println("실행 시간(ms): " + diffTime1); // 세컨드(초 단위 변환)

//        // bulk insert 방식 : 실행 시간(ms): 340 , 실행 시간(ms): 405
//        long beforeTime1 = System.currentTimeMillis();
//        GoodsService.bulkInsertBooks(excelDatas);
//        long afterTime1 = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
//        long diffTime1 = afterTime1 - beforeTime1; // 두 개의 실행 시간
//        System.out.println("실행 시간(ms): " + diffTime1); // 세컨드(초 단위 변환)*/

        return ResponseEntity.ok(resultList);
//        return ResponseEntity.ok(Collections.emptyMap());
    }

    private File getFile(MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();

        String directoryPath = System.getProperty("user.dir") + "/uploads/";  // 리소스 경로로 설정

        // 디렉토리 경로와 파일명을 결합하여 전체 파일 경로 생성
        String filePath = directoryPath + originalFileName;

        // 파일을 저장할 경로 객체
        Path path = Paths.get(filePath);

        // 파일이 존재하지 않으면 디렉토리 생성
        if (!Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }

        // 파일을 지정된 경로에 저장
        file.transferTo(path.toFile());  // MultipartFile을 디스크에 저장

        // 저장된 파일 객체 반환
        return path.toFile();  // File 객체 반환
    }

    @GetMapping("/steadySeller")
    public String steadySeller(@ModelAttribute("member") Member member,
                               @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                               @RequestParam(value = "size", required = false, defaultValue = "20") int size,
                               Model model) {
        return prepareSellerPage(member, page, size, model, "user/userSteadyseller");
    }

    private String prepareSellerPage(Member member, int page, int size, Model model, String viewName) {
        // 카테고리 데이터 가져오기
        List<Category> categoryList = categoryService.getCategoryList();
        model.addAttribute("categoryList", categoryList);

        // 회원 번호 가져오기
        Long memberNo = null;
        if (member != null) {
            memberNo = member.getMemberNo();
        }

        // 찜 상태가 포함된 상품 목록 조회
        List<Goods> bestGoodsList = goodsService.getBestGoodsWishStatus(memberNo, page, size);
        model.addAttribute("bestGoodsList", bestGoodsList);

        // 대표 이미지 가져오기
        for (Goods goods : bestGoodsList) {
            GoodsImage mainImage = goodsService.getMainImageByGoodsNo(goods.getGoodsNo());
            goods.setMainImage(mainImage);
        }

        // 찜 상태가 포함된 상품 목록 조회
        List<Goods> steadyGoodsList = goodsService.getSteadyGoodsWishStatus(memberNo, page, size);
        model.addAttribute("steadyGoodsList", steadyGoodsList);

        // 대표 이미지 가져오기
        for (Goods goods : steadyGoodsList) {
            GoodsImage mainImage = goodsService.getMainImageByGoodsNo(goods.getGoodsNo());
            goods.setMainImage(mainImage);
        }

        // 페이지네이션 정보 전달
        int totalItems = Math.min(goodsService.getTotalBestGoodsCount(memberNo), 100);
        int totalPages = (int) Math.ceil((double) totalItems / size);

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("size", size);
        return viewName;
    }

    @GetMapping("/newItems")
    public String NewItems(@ModelAttribute("member") Member member,
                           @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                           @RequestParam(value = "size", required = false, defaultValue = "20") int size,
                           @RequestParam(value = "year", required = false) String year,
                           @RequestParam(value = "month", required = false) String month,
                           @RequestParam(value = "week", required = false) String week,
                           Model model) {

        // 카테고리 데이터 가져오기
        List<Category> categoryList = categoryService.getCategoryList();
        model.addAttribute("categoryList", categoryList);

        // 회원 번호 가져오기
        Long memberNo = null;
        if (member != null) {
            memberNo = member.getMemberNo();
        }

        // 신상품 목록 조회: 정렬 방식과 일자 필터 적용
        List<Goods> goodsList = goodsService.getNewItems(memberNo, year, month, week, page, size);
        model.addAttribute("goodsList", goodsList);


        // 대표 이미지 가져오기
        for (Goods goods : goodsList) {
            GoodsImage mainImage = goodsService.getMainImageByGoodsNo(goods.getGoodsNo());
            goods.setMainImage(mainImage);
        }

        // 페이지네이션 정보 전달
        int actualTotalItems = goodsService.getTotalNewItemsCount(memberNo, year, month, week);
        int totalItems = Math.min(actualTotalItems, 100); // 최대 100개로 제한
        int totalPages = (int) Math.ceil((double) totalItems / size);

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("size", size);
        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("week", week);

        return "user/userNewItems";
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
        if (goods != null) {
            return ResponseEntity.ok(goods);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
//    @PostMapping("/updateGoods")
//    public ResponseEntity<String> updateGoods(@RequestPart("goods") Goods goods,
//                                              @RequestPart(value="goodsImages", required=false) List<MultipartFile> goodsImages) {
//        // goods 객체 및 첨부파일(goodsImages)을 이용하여 상품 업데이트 로직 수행
//        try {
//            goodsService.updateGoods(goods, goodsImages);
//            return ResponseEntity.ok("상품 업데이트 성공");
//        } catch(Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("상품 업데이트 실패: " + e.getMessage());
//        }
//    }
}
