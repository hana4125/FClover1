package hello.fclover.mybatis.mapper;

import hello.fclover.dto.SearchLogDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SearchLogMapper {

    void insertSearchLog(SearchLogDTO searchLogDTO);


}
