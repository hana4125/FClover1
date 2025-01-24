package hello.fclover.service;



import hello.fclover.domain.Notice;
import hello.fclover.domain.Question;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

public interface QuestionService {

    default public String saveUploadFile(MultipartFile uploadfile, String saveFolder) throws Exception {

        String originalFilename = uploadfile.getOriginalFilename();
        String fileDBName = fileDBName(originalFilename, saveFolder);

        //파일 저장
        uploadfile.transferTo(new File(saveFolder + fileDBName));
        return fileDBName;
    }

    default public String fileDBName(String filename, String saveFolder) {

        String dateFolder = createFolderByDate(saveFolder);
        String fileExtension = getFileExtension(filename);
        String refileName = generateUniqueFileName(fileExtension);

        return File.separator + dateFolder + File.separator + refileName;
    }

    default public  String getFileExtension(String fileName) {

        int index = fileName.lastIndexOf(".");
        return (index > 0 ) ? fileName.substring(index + 1) : "";
    }

    default public int[] getCurrentDate(){

        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int date = now.getDayOfMonth();

        return new int[]{year, month, date};
    }

    default public String createFolderByDate(String baseFolder) {

        int[] currentDate = getCurrentDate();
        int year = currentDate[0];
        int month = currentDate[1];
        int date = currentDate[2];

        String dateFolder = year + "-" + month + "-" + date;
        String fullFolderPath = baseFolder + File.separator + dateFolder;
        File path = new File(fullFolderPath);

        if (!path.exists()) {
            path.mkdirs();
            System.out.println("디렉토리 생성");
        }
        return dateFolder;
    }

    default public String generateUniqueFileName(String fileExtension) {

        int[] currentDate = getCurrentDate();
        int year = currentDate[0];
        int month = currentDate[1];
        int date = currentDate[2];

        Random r = new Random();
        int random = r.nextInt(100000000);

        return "bbs" + year + month + date + random + "." + fileExtension;
    }


    int TotalCount();

    List<Question> BoardList(Integer currentPage, int limit);

    void insertQuestion(Question question);

    Question Detail(int no);



    //댓글 목록 가져오기
    public List<Question> getCommentList(int board_num, int page);

    //댓글 등록하기
    public int commentsInsert(Question c);

    //댓글 삭제
    public int commentDelete(int num);

    //댓글 수정
    public int commentsUpdate(Question co);

    String getQvalue(String qtype);
}
