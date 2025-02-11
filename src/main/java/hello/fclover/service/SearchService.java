package hello.fclover.service;

import hello.fclover.domain.Member;
import hello.fclover.dto.SearchDetailParamDTO;
import hello.fclover.dto.SearchParamDTO;
import hello.fclover.dto.SearchResponseDTO;

public interface SearchService {

    SearchResponseDTO searchByKeyword(String keyword, String sessionId, Member member);

    SearchResponseDTO searchDetail(SearchDetailParamDTO searchDetailParamDTO);

    SearchResponseDTO refineResult(SearchParamDTO searchParamDTO);
}
