package hello.fclover.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchDetailForm {

    private String cname;

    private String chrcDetail;

    private String pbcmDetail;

    private String repKeyword;

    private String cate;

    private String repKeywordTarget;

    private Integer saprmin;

    private Integer saprmax;

    private Integer rlseStr;

    private Integer rlseEnd;

}
