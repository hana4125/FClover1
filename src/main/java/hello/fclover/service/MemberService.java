package hello.fclover.service;

import hello.fclover.domain.Member;

public interface MemberService {
    public int isId(String id,String pass);

    public int insert(Member m);

    public int isId(String id);

}
