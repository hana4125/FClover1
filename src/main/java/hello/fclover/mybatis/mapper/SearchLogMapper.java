package hello.fclover.mybatis.mapper;

import hello.fclover.dto.SearchLogDTO;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SearchLogMapper {

    void insertSearchLog(SearchLogDTO searchLogDTO);

    List<SearchLogDTO> findLogsBetween(@Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
}
