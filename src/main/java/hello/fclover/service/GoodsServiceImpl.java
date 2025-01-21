package hello.fclover.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import hello.fclover.domain.Goods;
import hello.fclover.domain.GoodsImage;
import hello.fclover.mybatis.mapper.GoodsImageMapper;
import hello.fclover.mybatis.mapper.GoodsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class GoodsServiceImpl implements GoodsService {

    private final GoodsMapper goodsMapper;
    private final GoodsImageMapper imageMapper;

    private final AmazonS3 amazonS3;

//    @Value("${cloud.aws.s3.bucket}")
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
    public List<Goods> getGoodsList(int cate_no, String sort, int page, int size) {
        int offset = (page - 1) * size;
        return goodsMapper.findAll(cate_no, sort, offset, size);
    }

    @Override
    public int getTotalGoodsCount(int cate_no) {
        return goodsMapper.countGoods(cate_no);
    }

    @Override
    public Goods findGoodsByNo(Long goodsNo) {
        return goodsMapper.findGoodsById(goodsNo);
    }

    @Override
    public void getGoodsDetail(Long goodsNo, Model model) {
        model.addAttribute("goods", goodsMapper.findGoodsById(goodsNo));
        List<String> imageList = getGoodsImages(goodsNo, goodsImage);
        model.addAttribute("imageList", imageList);
    }

    private List<String> getGoodsImages(Long goodsNo, GoodsImage goodsImage) {
        //상품 번호에 맞는 이미지들 이름 가져오기
        List<Map<String, String>> imageNames = imageMapper.findAllGoodsImage(goodsNo);
        //이미지 파일 담을 List
        List<String> images = new ArrayList<>();
        String result = null;
        for (Map<String, String> imageName : imageNames) {
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                //캐시 사용 설정 해제
                ImageIO.setUseCache(false);
                //아마자 경로
                String name = imageName.get("goods_image_name");
                String imageUrl = imageName.get("goods_url") + File.separator + name;

                images.add(imageUrl);
            } catch (Exception e) {
                System.out.println("e.getMessage() = " + e.getMessage());
            }

        }
        return images;
    }

    private void goodsInsertImage(List<MultipartFile> images, String sellerNumber, int goodsNo) throws IOException {
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

                amazonS3.putObject(bucket, imageSaveFolder + File.separator, new ByteArrayInputStream(new byte[0]), new ObjectMetadata());

                objectMetadata.setContentType(image.getContentType());
                objectMetadata.setContentLength(image.getSize());
                objectMetadata.setHeader("filename", image.getOriginalFilename());
                amazonS3.putObject(new PutObjectRequest(bucket + imageSaveFolder, imageDBName, image.getInputStream(), objectMetadata));

                imageUrl = bucket  + File.separator + imageSaveFolder;

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
