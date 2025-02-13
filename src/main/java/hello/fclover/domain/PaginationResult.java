package hello.fclover.domain;

public class PaginationResult {
    private int page;
    private int limit;
    private int totalcount;
    private int maxpage;
    private int startpage;
    private int endpage;
    private final int pageLimit = 10;

    public PaginationResult(int page, int limit, int listcount) {
        this.page = page;
        this.limit = limit;
        this.totalcount = listcount;

        if (listcount == 0) {
            // 데이터가 없는 경우
            this.maxpage = 1;
            this.startpage = 1;
            this.endpage = 1;
        } else {
            // 데이터가 있는 경우
            this.maxpage = (int) Math.ceil((double) listcount / limit);
            this.startpage = ((page - 1) / pageLimit) * pageLimit + 1;
            this.endpage = startpage + pageLimit - 1;

            if (this.endpage > this.maxpage) {
                this.endpage = this.maxpage;
            }
        }
    }

    public int getMaxpage() {
        return maxpage;
    }

    public int getStartpage() {
        return startpage;
    }

    public int getEndpage() {
        return endpage;
    }
}