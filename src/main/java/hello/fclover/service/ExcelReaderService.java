package hello.fclover.service;


import hello.fclover.domain.MessGoods;
import hello.fclover.mybatis.mapper.CategoryMapper;
import hello.fclover.mybatis.mapper.SellerMapper;
import hello.fclover.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelReaderService {
    private static final long MAX_FILE_SIZE_THRESHOLD = 30000L;
    private final CategoryMapper categoryMapper;
    private final SellerMapper sellerMapper;

    /* 파일의 사이즈의 따라 분기 치는 코드 */
    public List<MessGoods> readExcel(MultipartFile file) throws Exception {
        List<MessGoods> dataList = new ArrayList<>();
        if (file.getSize() < MAX_FILE_SIZE_THRESHOLD) {
//            dataList = readExcelWithXSSF(file.getInputStream());
        } else {
            dataList = readExcelWithSAX(FileUtil.saveAndGetFile(file));
        }

        return dataList;
    }

    /**
     * SAX 기반 엑셀 파일 읽기
     */
    public List<MessGoods> readExcelWithSAX(File file) throws Exception {
        ExcelSheetHandler excelSheetHandler = ExcelSheetHandler.readExcel(file);

        List<MessGoods> excelDatas = excelSheetHandler.getMessGoods();

        return excelDatas;
    }


//    /**
//     * XSSFWorkbook 기반 엑셀 파일 읽기
//     */
//    public List<MessGoods> readExcelWithXSSF(InputStream inputStream) {
//        List<MessGoods> dataList = new ArrayList<>();
//
//        try (Workbook workbook = new XSSFWorkbook(inputStream)) { // XSSFWorkbook 사용
//            Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트 가져오기
//
//            for (Row row : sheet) {
//                List<String> rowData = new ArrayList<>();
//                for (Cell cell : row) {
//                    rowData.add(getCellValue(cell));
//                }
//                dataList.add(new MessGoods());
//            }
//        } catch (Exception e) {
//            log.error("XSSFWorkbook 방식 엑셀 파일 읽기 오류", e);
//        }
//
//        return dataList;
//    }

    /**
     * SXSSFWorkbook는 읽기지원을 하지 않음 -> 해당 코드는 정상적으로 작동하지 않음.
     */
//    public List<MessGoods> readExcelWithSXSSF(InputStream inputStream) {
//        List<MessGoods> dataList = new ArrayList<>();
//
//        try (Workbook workbook = new XSSFWorkbook(inputStream)) { // XSSFWorkbook 사용
//            Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트 가져오기
//            for (Row row : sheet) {
//                if (row.getRowNum() == 1 || getCellValue(row.getCell(1)).equals("")) continue; // 첫 번째 행(헤더) 건너뛰기
//
//                String cateName = getCellValue(row.getCell(0));
//                int cateNo = categoryMapper.findcateNo(cateName);
//
//                String goodsName = getCellValue(row.getCell(1));
//                String goodsContent = getCellValue(row.getCell(2));
//                int goodsPrice = parsePrice(getCellValue(row.getCell(3)));
//                String goodsWriter = getCellValue(row.getCell(4));
//                String writerContent = getCellValue(row.getCell(5));
//                LocalDate goodsCreateAt = LocalDate.parse(getCellValue(row.getCell(6)));
//                int goodsPageCount = Integer.parseInt(getCellValue(row.getCell(7)));
//                String goodsBookSize = getCellValue(row.getCell(8));
//                String companyName = getCellValue(row.getCell(9));
//                long sellerNo = sellerMapper.findSellerNo(companyName);
//                String mainImage = getCellValue(row.getCell(10));
//                String imageUrl = getExcelUrl(mainImage);
//
//                String subImage1 = getCellValue(row.getCell(11));
//                String subImage2 = getCellValue(row.getCell(12));
//
//                dataList.add(new MessGoods(cateNo, goodsName, goodsContent, goodsPrice, goodsWriter, writerContent, goodsCreateAt, goodsPageCount, goodsBookSize, sellerNo, imageUrl, mainImage, subImage1, subImage2));
//            }
//        } catch (Exception e) {
//            log.error("XSSFWorkbook 방식 엑셀 파일 읽기 오류", e);
//        }
//
//        return dataList;
//    }
//
//    private int parsePrice(String priceStr) {
//        try {
//            return (int) Double.parseDouble(priceStr); // 소수점이 포함된 경우 정수로 변환
//        } catch (NumberFormatException e) {
//            log.warn("가격 변환 오류: {}", priceStr);
//            return 0; // 변환 실패 시 기본값 처리
//        }
//    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            case BLANK, ERROR -> "";
            default -> "";
        };
    }

    private String getExcelUrl(String mainImage) {
        int lastIndexOf = mainImage.lastIndexOf("/");
        return mainImage.substring(0, lastIndexOf + 1);
    }

    /**
     * 현재 JVM 사용 메모리 측정
     */
    private long getUsedMemory() {
        System.gc(); // GC 실행 후 메모리 측정 (더 정확한 값 얻기 위해)
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }
}
