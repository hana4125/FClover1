package hello.fclover.service;

import hello.fclover.domain.Delivery;
import hello.fclover.domain.Member;

import java.util.List;

public interface MemberService {

    int signup(Member member);

    int isId(String id);

    int isId(String id, String password);

    Member getMember(String id);

    int updateMember(Member member);

    int addDeliveryAddress(Delivery delivery);

    List<Delivery> getDeliveryAddress(String member_id);

    Member isMemberExists(String member_id, String password);

    String getEncryptedPassword(String member_id);

    void removeAccount(String member_id);
}
