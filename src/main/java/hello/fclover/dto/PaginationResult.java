package hello.fclover.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationResult<T> {
    private List<T> items;
    private int currentPage;
    private int totalPages;
    private int pageSize;
    private String sort;

}

