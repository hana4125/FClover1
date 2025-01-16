package hello.fclover.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class Question {
    private int qno;
    private String qcreateat;
    private String memberid;
    private String qresponseat;
    private String qname;
    private String qtype;
    private String qfile;
    private String qtitle;
    private String qcontent;
    private String responsephone;
    private String responseemail;
    private String qalert;
    private int qreref;
    private int qrelev;
    private int qreseq;
    private MultipartFile uploadfile;
}
