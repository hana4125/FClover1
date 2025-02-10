package hello.fclover.service;

import hello.fclover.domain.MessGoods;
import org.apache.poi.ooxml.util.SAXHelper;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.ContentHandler;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ExcelSheetHandler implements XSSFSheetXMLHandler.SheetContentsHandler {

    private int currentCol = -1;
    private int currRowNum = 0;

    private List<MessGoods> messGoods = new ArrayList<>();    //실제 엑셀을 파싱해서 담아지는 데이터 GoodsDomain
    private List<List<String>> rows = new ArrayList<List<String>>();
    private MessGoods row = new MessGoods(); //한 행의 데이터 저장.
    private List<String> header = new ArrayList<>();

    public static ExcelSheetHandler readExcel(File file) throws Exception {

        ExcelSheetHandler sheetHandler = new ExcelSheetHandler();
        try {
            OPCPackage opc = OPCPackage.open(file);
            XSSFReader xssfReader = new XSSFReader(opc);
            StylesTable styles = xssfReader.getStylesTable();
            ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);

            //엑셀의 시트를 하나만 가져오기.
            //여러개일경우 iter문으로 추출해야 함. (iter문으로)
            InputStream inputStream = xssfReader.getSheetsData().next();
            InputSource inputSource = new InputSource(inputStream);
            ContentHandler handle = new XSSFSheetXMLHandler(styles, strings, sheetHandler, false);

            XMLReader xmlReader = SAXHelper.newXMLReader();
            xmlReader.setContentHandler(handle);

            xmlReader.parse(inputSource);
            inputStream.close();
            opc.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return sheetHandler;

    }

    public List<MessGoods> getMessGoods() {
        return messGoods;
    }



    @Override
    public void startRow(int rowNum) {
        this.currentCol = -1;
        this.currRowNum = rowNum;
        this.row = new MessGoods();
    }

    @Override
    public void cell(String columnName, String value, XSSFComment comment) {

        int iCol = (new CellReference(columnName)).getCol();
        int currentCol = iCol;
        if(currRowNum > 0) {
            switch (iCol) {
                case 0: //제목
                    row.setCateName(value);
                    break;
                case 1:
                    row.setGoodsName(value);
                    break;
                case 2:
                    row.setGoodsContent(value);
                    break;
                case 3:
                    row.setGoodsPrice(Integer.parseInt(value));
                    break;
                case 4:
                    row.setGoodsWriter(value);
                    break;
                case 5:
                    row.setWriterContent(value);
                    break;
                case 6:
                    row.setGoodsCreateAt(value);
                    break;
                case 7:
                    row.setGoodsPageCount(Integer.parseInt(value));
                    break;
                case 8:
                    row.setGoodsBookSize(value);
                    break;
                case 9:
                    row.setSellerName(value);
                    break;
                case 10:
                    row.setMainImage(value);
                    break;
                case 11:
                    row.setSubImage1(value);
                    break;
                case 12:
                    row.setSubImage2(value);
                    break;
                default:
                    break;
            }
        }

    }

    @Override
    public void endRow(int rowNum) {
//        if (rowNum == 1) {
//            // 첫 번째 행을 헤더로 설정
//            header = List.of(String.valueOf(row.getCateNo()));
//        } else {
            if (row.getGoodsName() != null) {
                messGoods.add(row);
            }
//        }
    }


    public void hyperlinkCell(String arg0, String arg1, String arg2, String arg3, XSSFComment arg4) {
        // TODO Auto-generated method stub

    }
}