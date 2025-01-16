package hello.fclover.domain;

public class PaginationResult {
    private int maxpage;
    private int startpage;
    private int endpage;


    public PaginationResult(int page, int limit, int listcount) {
        //총 페이지 수
        int maxpage = (listcount + limit - 1) / limit;
        //현재 페이지에 보여줄 시작 페이지 수
        int startpage = ((page - 1) / 10) * 10 +1;
        //현재 페이지에 보여줄 마지막 페이지 수
        int endpage = startpage + 10 -1;

        if(endpage > maxpage)
            endpage = maxpage;

        this.maxpage = maxpage;
        this.startpage = startpage;
        this.endpage = endpage;

    }

    public Object getMaxpage() {
        return maxpage;
    }

    public Object getStartpage() {
        return startpage;
    }

    public Object getEndpage() {
        return endpage;
    }
}
