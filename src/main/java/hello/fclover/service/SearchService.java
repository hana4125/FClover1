package hello.fclover.service;

import hello.fclover.dto.SearchDetailParamDTO;
import hello.fclover.dto.SearchParamDTO;
import hello.fclover.dto.SearchResponseDTO;
import java.util.Map;

public interface SearchService {

    int countByKeyword(String keyword);

    SearchResponseDTO searchByKeyword(String keyword);

    SearchResponseDTO searchDetail(SearchDetailParamDTO searchDetailParamDTO);

    SearchResponseDTO refineResult(SearchParamDTO searchParamDTO);
}
