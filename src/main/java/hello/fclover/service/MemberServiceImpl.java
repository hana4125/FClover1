package hello.fclover.service;

import hello.fclover.domain.Member;
import hello.fclover.mybatis.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private MemberMapper dao;
    private PasswordEncoder passwordEncoder;

    public MemberServiceImpl(MemberMapper dao) {
        this.dao = dao;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }


    @Override
    public int isId(String id, String password) {
        int result = -1; //아이디 존재하지 않는 경우
        Member rmember = dao.isId(id);

        if(rmember != null) { //아이디가 존재하는 경우
            if(passwordEncoder.matches(password,rmember.getPassword())) {
                result = 1; //아이디와 비밀번호가 일치하는 경우
            }else
                result=0; //아이디는 존재하지만 비밀번호가 일치하지 않는 경우

        }
        return result;
    }

    @Override
    public int insert(Member m) {

        return dao.insert(m);
    }

    @Override
    public int isId(String id) {
        Member rmember = dao.isId(id);

        return (rmember ==null)? -1 : 1; //-1은 아이디가 존재하지 않는 경우
        //1은 아이디가 존재하는 경우
    }
}
