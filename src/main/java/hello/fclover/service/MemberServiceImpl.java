package hello.fclover.service;

import hello.fclover.domain.AddressBook;
import hello.fclover.domain.Member;
import hello.fclover.mybatis.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public int getMemNum(String memberId) {
        return dao.selectMemNum(memberId);
    }

    @Override
    public Member getMember(String id) {
        return dao.isId(id);
    }

    @Override
    public int updateMember(Member member) {
        return dao.updateMember(member);
    }

    @Override
    public int addDeliveryAddress(AddressBook addressBook) {
        return dao.insertAddressBook(addressBook);
    }

    @Override
    @Transactional
    public void setDefaultAddress(int addressId) {
        dao.updateDefaultAddress(addressId);
    }

    @Override
    public List<AddressBook> getDeliveryAddress(int memNum) {
        return dao.selectAddressBook(memNum);
    }

    @Override
    public Member isMemberExists(String memberId, String password) {
        return dao.selectMember(memberId, password);
    }

    @Override
    public String getEncryptedPassword(String memberId) {
        return dao.selectPassword(memberId);
    }

    @Override
    public void removeAccount(String memberId) {
        dao.deleteMember(memberId);
    }

    @Override
    public AddressBook getDefaultAddress(int memNum) {
        return dao.selectDefaultAddress(memNum);
    }
}
