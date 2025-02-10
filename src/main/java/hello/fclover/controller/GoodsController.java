package hello.fclover.controller;


import hello.fclover.domain.Goods;
import hello.fclover.domain.MessGoods;
import hello.fclover.mybatis.mapper.GoodsMapper;
import hello.fclover.mybatis.mapper.MessGoodsMapper;
import hello.fclover.service.ExcelSheetHandler;
import hello.fclover.service.GoodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.*;


@Controller
@RequestMapping("/goods")
@RequiredArgsConstructor
public class GoodsController {
    private final GoodsService goodsService;
    private final GoodsMapper goodsMapper;
    private final MessGoodsMapper messGoodsMapper;
    @GetMapping("/GoodsDetail/{no}")
    public String goodsDetail(Model model, @PathVariable("no") Long goodsNo) {
        goodsService.getGoodsDetail(goodsNo, model);
        System.out.println("model = " + model.getAttribute("imageList"));
        return "user/userGoodsDetail";
    }

    @PostMapping("/addMassProductProcess")
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

//        return ResponseEntity.ok(resultList);
        return ResponseEntity.ok(Collections.emptyMap());
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

}
