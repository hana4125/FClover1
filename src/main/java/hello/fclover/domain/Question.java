package hello.fclover.domain;

import hello.fclover.oauth2.dto.OAuth2Response;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class Question {
    private int qno;
    private String qcreateat;
    private String memberid;
    private String qname;
    private String qtype;
    private String qfile;
    private String qtitle;
    private String qcontent;
    private String responsephone;
    private String responseemail;
    private boolean qalert=true;
    private int qreref;
    private int qrelev;
    private int qreseq;
    private MultipartFile uploadfile;

    private int cno;
    private String ccontent;
    private String cresponseat;


    public String getEmail() {
        return this.responseemail;
    }

    public String getPhone() {
        return this.responsephone;
    }

    public Object getQalert() {
        return this.qalert;
    }


}
