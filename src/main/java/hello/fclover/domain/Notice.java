package hello.fclover.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Notice {
    private int notino;
    private String notititle;
    private String notiname;
    private String noticontent;
    private String notidate;

    public int getNotino() {
        return notino;
    }

    public void setNotino(int notino) {
        this.notino = notino;
    }

    public String getNotititle() {
        return notititle;
    }

    public void setNotititle(String notititle) {
        this.notititle = notititle;
    }

    public String getNotiname() {
        return notiname;
    }

    public void setNotiname(String notiname) {
        this.notiname = notiname;
    }

    public String getNoticontent() {
        return noticontent;
    }

    public void setNoticontent(String noticontent) {
        this.noticontent = noticontent;
    }

    public String getNotidate() {
        return notidate;
    }

    public void setNotidate(String notidate) {
        this.notidate = notidate;
    }
}