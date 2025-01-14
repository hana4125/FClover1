package hello.fclover.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Inquiry {
    private int qno;
    private String qcreateat;
    private String memberid;
    private String qresponseat;
    private String q_name;
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
}
