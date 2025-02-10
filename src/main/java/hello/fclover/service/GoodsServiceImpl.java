package hello.fclover.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import hello.fclover.domain.Goods;
import hello.fclover.domain.GoodsImage;
import hello.fclover.domain.MessGoods;
import hello.fclover.mybatis.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class GoodsServiceImpl implements GoodsService {

    private final GoodsMapper goodsMapper;
    private final GoodsImageMapper imageMapper;
    private final ExcelReaderService excelReaderService;
    private final MessGoodsMapper messGoodsMapper;
    private final CategoryMapper categoryMapper;
    private final SellerMapper sellerMapper;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    String bucket;

    GoodsImage goodsImage = new GoodsImage();

    @Override
    @Transactional
    public void goodsSingleInsert(Goods goods, List<MultipartFile> images, String sellerNumber) throws IOException {
        int result = goodsMapper.goodsInsertText(goods);
        Long goodsNo = goodsMapper.goodsNoselect(goods.getSellerNo(), goods.getGoodsName());
        System.out.println(goodsNo);
        if (result > 0 && images.size() > 0) {
            goodsInsertImage(images, sellerNumber, goodsNo);
        }
    }

    @Override
    public int getTotalGoodsCount(int cateNo) {
        return goodsMapper.countGoods(cateNo);
    }

    @Override
    public int getTotalBestGoodsCount(Long memberNo) {
        return goodsMapper.countBestGoods();
    }

    @Override
    public Goods findGoodsByNo(Long goodsNo) {
        return goodsMapper.findGoodsById(goodsNo);
    }

    @Override
    public List<Goods> getCategoryGoodsList(int cateNo, String sort, int page, int size) {
        int offset = (page - 1) * size;
        System.out.println("cateNo=" + cateNo + ", page=" + page + ", size=" + size + ", offset=" + offset);
        return goodsMapper.findCategoryGoodsWishStatus(cateNo, sort, offset, size);
    }

    @Override
    public List<Goods> getBestGoodsList(int limit) {
        return goodsMapper.findByBestRank(limit);
    }

    @Override
    public List<Goods> getSteadyGoodsList(int limit) {
        return goodsMapper.findBySteadyRank(limit);
    }

    @Override
    public List<Goods> getBestGoodsWishStatus(Long memberNo, int page, int size) {
        int offset = (page - 1) * size;
        return goodsMapper.findBestGoodsWishStatus(memberNo, offset, size);
    }

    @Override
    public List<Goods> getSteadyGoodsWishStatus(Long memberNo, int page, int size) {
        int offset = (page - 1) * size;
        return goodsMapper.findSteadyGoodsWishStatus(memberNo, offset, size);
    }

    public void getGoodsDetail(Long goodsNo, Model model) {

        Goods goods = goodsMapper.findGoodsById(goodsNo);
        model.addAttribute("goods", goods);

        List<String> imageList = getGoodsImages(goodsNo);
        model.addAttribute("imageList", imageList);

    }

    @Override
    public GoodsImage getMainImageByGoodsNo(Long goodsNo) {
        return imageMapper.findMainImageByGoodsNo(goodsNo);
    }

    @Override
    public List<Goods> getNewItems(Long memberNo, String year, String month, String week, int page, int size) {
        int offset = (page - 1) * size;
        // 최대 100개까지만 조회되도록 offset과 size를 조정
        if (offset >= 100) {
            return List.of(); // 빈 리스트 반환
        }
        if (offset + size > 100) {
            size = 100 - offset;
        }
        return goodsMapper.selectNewItems(memberNo, year, month, week, offset, size);
    }

    @Override
    public int getTotalNewItemsCount(Long memberNo, String year, String month, String week) {
        int count = goodsMapper.countNewItems(memberNo, year, month, week);
        return Math.min(count, 100); // 최대 100개로 제한
    }

    @Override
    public List<Goods> sellerGoodsSearch(Map<String, String> searchKeyword) {
        return goodsMapper.sellerGoodsSearch(searchKeyword);
    }

//     private List<String> getGoodsImages(Long goodsNo) {

//    @Transactional
//    public void saveProduct(InputStream inputStream){
//        List<MessGoods> data = excelReaderService.readExcelWithXSSF(inputStream);
    // 1방식 순차처리
//        data.forEach(messGoodsMapper::saveProduct);

    // 2방식 병렬처리
//        data.parallelStream().forEach(messGoodsMapper::saveProduct);


    //bulkInsertBooks(data);
    // 병렬 스트림을 사용하여 데이터 삽입
//    }
  

    @Override
    public Map<String, Object> saveMessproduct(List<MessGoods> messGoods) {
        List<MessGoods> goodsInsertSuccess = new ArrayList<>();
        List<MessGoods> goodsInsertFail = new ArrayList<>();
        for (MessGoods messGood : messGoods) {
            Goods goods = new Goods();
            GoodsImage goodsImage = new GoodsImage();
//            try {
                goods.setCateNo(categoryMapper.findCateNo(messGood.getCateName()));
                goods.setGoodsName(messGood.getGoodsName());
                goods.setGoodsContent(messGood.getGoodsContent());
                goods.setGoodsPrice(messGood.getGoodsPrice());
                goods.setGoodsWriter(messGood.getGoodsWriter());
                goods.setWriterContent(String.valueOf(messGood.getWriterContent()));
                goods.setCompanyName(messGood.getSellerName());
                // 입력 형식에 맞는 DateTimeFormatter 생성
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                // 문자열을 LocalDate로 파싱
                goods.setGoodsCreateAt(LocalDate.parse(messGood.getGoodsCreateAt(), formatter));

                goods.setGoodsPageCount(messGood.getGoodsPageCount());
                goods.setGoodsBookSize(messGood.getGoodsBookSize());
                goods.setSellerNo(sellerMapper.findSellerNo(messGood.getSellerName()));
                messGoodsMapper.saveProduct(goods);
//            }catch (Exception e) {
//                goodsInsertFail.add(messGood);
//                continue;
//            }

            goodsImage.setGoodsNo(goodsMapper.goodsNoselect(goods.getSellerNo(), goods.getGoodsName()));
            String mainImage = messGood.getMainImage();
            String imageUrl = getExcelUrl(mainImage);
//            try {
                if (messGood.getMainImage() != null) {
                    goodsImage.setGoodsImageName(getExcelImageName(messGood.getMainImage()));
                    goodsImage.setGoodsUrl(imageUrl);
                    goodsImage.setIsMain("M");
                    messGoodsMapper.saveProductImage(goodsImage);
                }
//            }catch (Exception e) {
//                goodsInsertFail.add(messGood);
//                continue;
//            }
//            try {
                if (messGood.getSubImage1() != null) {
                    goodsImage.setGoodsImageName(getExcelImageName(messGood.getSubImage1()));
                    goodsImage.setIsMain("S");
                    messGoodsMapper.saveProductImage(goodsImage);
                }
//            }catch (Exception e) {
//                goodsInsertFail.add(messGood);
//                continue;
//            }
//            try{
            if(messGood.getSubImage2() != null) {
                goodsImage.setGoodsImageName(getExcelImageName(messGood.getSubImage2()));
                goodsImage.setIsMain("S");
                messGoodsMapper.saveProductImage(goodsImage);
            }
//            }catch (Exception e) {
//                goodsInsertFail.add(messGood);
//            }
            goodsInsertSuccess.add(messGood);
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("goodsInsertSuccess", goodsInsertSuccess);
        resultMap.put("goodsInsertFail", goodsInsertFail);

        return resultMap;
    }

    private String getExcelUrl(String mainImage) {
        int lastIndexOf = mainImage.lastIndexOf("/");
        return mainImage.substring(0, lastIndexOf + 1);
    }

    private String getExcelImageName(String mainImage) {
        int lastIndexOf = mainImage.lastIndexOf("/");
        return mainImage.substring(lastIndexOf + 1);
    }

    private List<String> getGoodsImages(Long goodsNo, GoodsImage goodsImage) {

        //상품 번호에 맞는 이미지들 이름 가져오기
        List<Map<String, String>> imageNames = imageMapper.findAllGoodsImage(goodsNo);

        System.out.println("imageNames = " + imageNames);
        //이미지 파일 담을 List
        List<String> images = new ArrayList<>();
        String result = null;
        for (Map<String, String> imageName : imageNames) {
            //이미지 경로
            String name = imageName.get("goods_image_name");
            String imageUrl = imageName.get("goods_url") + File.separator + name;

            images.add(imageUrl);


        }
        return images;
    }

    private void goodsInsertImage(List<MultipartFile> images, String sellerNumber, Long goodsNo) throws IOException {
        boolean IsFirstImage = true;
        ObjectMetadata objectMetadata = new ObjectMetadata();

        String imageDBName = "";
        String imageSaveFolder = "";
        String imageUrl = "";
        for (MultipartFile image : images) {
            if (!images.isEmpty()) {
                imageDBName = imageReName(image.getOriginalFilename());
                System.out.println("imageDBName = " + imageDBName);
                //이미지 저장 폴더 이름 = 사업자 등록 번호
                imageSaveFolder = sellerNumber.replace("-", "");

//                amazonS3.putObject(bucket, imageSaveFolder + File.separator, new ByteArrayInputStream(new byte[0]), new ObjectMetadata());

                objectMetadata.setContentType(image.getContentType());
                objectMetadata.setContentLength(image.getSize());
                objectMetadata.setHeader("filename", image.getOriginalFilename());
                amazonS3.putObject(new PutObjectRequest(bucket, imageSaveFolder + File.separator + imageDBName, image.getInputStream(), objectMetadata));

                imageUrl = getS3Url(imageDBName) + imageSaveFolder;

            }
            if (IsFirstImage) {
                //상품 내용 저장 및 대표 사진 저장, goods_image에 goodsNo 전달
                goodsImage.setIsMain("M");
                IsFirstImage = false;
            } else {
                goodsImage.setIsMain("S");
            }
            System.out.println("goodsImage = " + goodsImage.getIsMain());
//            //로컬 파일 업로드
//            image.transferTo(new File(imageUrl, imageDBName));

            goodsImage.setGoodsNo(Long.valueOf(goodsNo));
            goodsImage.setGoodsUrl(imageUrl);
            goodsImage.setGoodsImageName(imageDBName);
            //상품 이미지 저장.
            imageMapper.goodsInsertImage(goodsImage);
        }
    }

    private String getS3Url(String imageDBName) {
        String urlString = amazonS3.getUrl(bucket, imageDBName).toString();
        int lastIndexOf = urlString.lastIndexOf("/");
        return urlString.substring(0, lastIndexOf + 1);
    }

    private String imageReName(String fileName) {
        LocalDate today = LocalDate.now();
        // "yyyyMMdd" 포맷 지정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        // 날짜 포맷팅
        String insertDate = today.format(formatter);

        UUID uuid = UUID.randomUUID();
        //확장자
        String extension = fileName.substring(fileName.lastIndexOf("."));

        return uuid.toString() + insertDate + extension;
    }


}
