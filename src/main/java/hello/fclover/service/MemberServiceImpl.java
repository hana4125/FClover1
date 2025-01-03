package hello.fclover.service;

import hello.fclover.domain.Delivery;
import hello.fclover.domain.Member;
import hello.fclover.mybatis.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberMapper dao;
    private final PasswordEncoder passwordEncoder;

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

    @Override
    public int addDeliveryAddress(Delivery delivery) {
        return dao.insertDeliveryAddress(delivery);
    }

    @Override
    public List<Delivery> getDeliveryAddress(String member_id) {
        return dao.selectDeliveryAddress(member_id);
    }

    @Override
    public Member isMemberExists(String member_id, String password) {
        return dao.selectMember(member_id, password);
    }

    @Override
    public String getEncryptedPassword(String member_id) {
        return dao.selectPassword(member_id);
    }

    @Override
    public void removeAccount(String member_id) {
        dao.deleteMember(member_id);
    }
}
