package hello.fclover.service;

import hello.fclover.domain.Goods;
import hello.fclover.domain.GoodsImage;
import hello.fclover.mybatis.mapper.GoodsImageMapper;
import hello.fclover.mybatis.mapper.GoodsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GoodsServiceImpl implements GoodsService {

    private final GoodsMapper goodsMapper;
    private final GoodsImageMapper imageMapper;
  
//    @Value("${goods.image.folder}")
    String saveFolder;
    GoodsImage goodsImage = new GoodsImage();
    @Override
    @Transactional
    public void goodsSingleInsert(Goods goods, List<MultipartFile> images, String sellerNumber) throws IOException {
        int result = goodsMapper.goodsInsertText(goods);

        int goodsNo = goodsMapper.goodsNoselect(String.valueOf(goods.getSellerNo()), goods.getGoodsName());
        System.out.println(goodsNo);
        if (result > 0 && images.size() > 0) {
            goodsInsertImage(images, sellerNumber, goodsNo);
        }
    }

//    @Override
//    public List<Goods> getGoodsList(int cate_no, String sort, int page, int size) {
//        int offset = (page - 1) * size;
//        return goodsMapper.findAll(cate_no, sort, offset, size);
//    }

    @Override
    public int getTotalGoodsCount(int cateNo) {
        return goodsMapper.countGoods(cateNo);
    }

    @Override
    public int getTotalBestGoodsCount() {
        return goodsMapper.countBestGoods();
    }

    @Override
    public Goods findGoodsByNo(Long goodsNo) {
        return goodsMapper.findGoodsById(goodsNo);
    }

    @Override
    public List<Goods> getGoodsWithWishStatusList(Long memberNo, int cateNo, String sort, int page, int size) {
        int offset = (page - 1) * size;
        return goodsMapper.findGoodsWithWishStatus(memberNo, cateNo, sort, offset, size);
    }

    @Override
    public List<Goods> getGoodsList() {
        return goodsMapper.findByRank();
    }

    @Override
    public List<Goods> getGoodsWishStatus(Long memberNo, int page, int size) {
        int offset = (page - 1) * size;
        return goodsMapper.findGoodsWishStatus(memberNo, offset, size);
    }

    private void goodsInsertImage(List<MultipartFile> images, String sellerNumber, int goodsNo) throws IOException {
        boolean IsFirstImage = true;
        //프로젝트 폴더 가져오기
        String projectFolder = System.getProperty("user.dir") + File.separator + saveFolder;

        String imageDBName="";
        String imageSaveFolder="";
        String imageUrl="";
        for (MultipartFile image : images) {
            if (!images.isEmpty()) {
                imageDBName = imageReName(image.getOriginalFilename());
                System.out.println("imageDBName = " + imageDBName);
                imageSaveFolder = imageFolder(projectFolder, sellerNumber);
                imageUrl = projectFolder + File.separator + imageSaveFolder;
            }
            if (IsFirstImage) {
                //상품 내용 저장 및 대표 사진 저장, goods_image에 goodsNo 전달
                goodsImage.setIsMain("M");
                IsFirstImage = false;
            }else{
                goodsImage.setIsMain("S");
            }
            System.out.println("goodsImage = " + goodsImage.getIsMain());
            //파일 업로드
            image.transferTo(new File(imageUrl, imageDBName));

            goodsImage.setGoodsNo(BigInteger.valueOf(goodsNo));
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

private String imageFolder(String projectFolder , String sellerNumber) {
    //이미지 저장 폴더 이름 = 사업자 등록 번호
    String imageFolder = sellerNumber.replace("-", "");
    String imageSaveFolder = projectFolder + File.separator + imageFolder;
    File path = new File(imageSaveFolder);
    System.out.println("path: " + path);
    if (!path.exists()) {
        path.mkdirs();
    }

    return imageFolder;
}



}
