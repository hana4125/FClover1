package hello.fclover.service;

import hello.fclover.domain.Member;
import hello.fclover.mybatis.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberMapper dao;
    private final PasswordEncoder passwordEncoder;



    @Override
    public int getListCount() {
        return dao.getListCount();
    }

    @Override
    public List<Member> getBoardList(int page, int limit) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        int startrow=(page-1)*limit; //0부터 10개, 10부터 10개

        map.put("start", startrow);
        map.put("limit", limit);
        return dao.getBoardList(map);
    }


    @Override
    public int signup(Member member) {
        return dao.insert(member);
    }

    @Override
    public int isId(String id) {
        Member rmember = dao.isId(id);
        return (rmember != null) ? -1 : 1;
    }

    @Override
    public int isId(String id, String password) {
        Member rmember = dao.isId(id);
        int result = -1;
        if (rmember != null) {
            if (passwordEncoder.matches(password, rmember.getPassword())) {
                result = 1;
            } else {
                result = 0;
            }
        }
        return result;
    }

    @Override
    public Member getMember(String id) {
        return dao.isId(id);
    }

    @Override
    public int updateMember(Member member) {
        return dao.memberUpdate(member);
    }


}
