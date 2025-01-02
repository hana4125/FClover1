package hello.fclover.service;

import hello.fclover.domain.Member;

import java.util.List;

public interface MemberService {

    int signup(Member member);

    int isId(String id);

    int isId(String id, String password);

    Member getMember(String id);

    int updateMember(Member member);

    int getListCount();

    List<Member> getBoardList(int page, int limit);


}
