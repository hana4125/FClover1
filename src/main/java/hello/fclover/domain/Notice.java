package hello.fclover.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Notice {
    private int NOTINUM;
    private String NOTITITLE;
    private String NOTINAME;
    private String NOTICONTENT;
    private String NOTIDATE;
}
