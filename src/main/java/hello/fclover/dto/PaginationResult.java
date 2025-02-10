package hello.fclover.dto;

import java.util.List;

public class PaginationResult<T> {
    private List<T> items;
    private int currentPage;
    private int totalPages;
    private int pageSize;
    private String sort;

    // 생성자, Getter 및 Setter

    public PaginationResult(List<T> items, int currentPage, int totalPages, int pageSize, String sort) {
        this.items = items;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.pageSize = pageSize;
        this.sort = sort;
    }

    // Getter 및 Setter
    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}

