package hello.fclover.service;

import hello.fclover.domain.AddressBook;
import hello.fclover.domain.Member;

import java.util.List;

public interface MemberService {

    int signup(Member member);

    int isId(String memberId);

    int isId(String memberId, String password);

    int getMemNum(String memberId);

    Member getMember(String memberId);

    int updateMember(Member member);

    int addDeliveryAddress(AddressBook addressBook);

    List<AddressBook> getDeliveryAddress(int memNum);

    Member isMemberExists(String memberId, String password);

    String getEncryptedPassword(String memberId);

    void removeAccount(String memberId);
}
