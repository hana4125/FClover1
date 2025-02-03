package hello.fclover.service;

import hello.fclover.dto.SearchDetailParamDTO;
import hello.fclover.dto.SearchParamDTO;
import hello.fclover.dto.SearchResponseDTO;
import java.util.Map;

public interface SearchService {

    int countByKeyword(String keyword);

    Map<String, Object> searchByKeyword(String keyword, String sort, int offset, int size);

    Map<String, Object> searchDetail(SearchDetailParamDTO searchDetailParamDTO, String sort, int offset, int size);

    SearchResponseDTO refineResult(SearchParamDTO searchParamDTO);
}
