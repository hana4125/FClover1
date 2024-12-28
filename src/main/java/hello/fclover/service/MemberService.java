package hello.fclover.service;

import hello.fclover.domain.Member;

public interface MemberService {

    int signup(Member member);

    int isId(String id);

    int isId(String id, String password);
}
