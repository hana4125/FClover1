package hello.fclover.service;

import hello.fclover.domain.AddressBook;
import hello.fclover.domain.Member;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

public interface MemberService {

    int signup(Member member);

    int getmemNo(String memberId);

    Member findMemberById(String memberId);

    String findMemberId(Member member);

    Integer selectMemberResetPassword(Member member);

    int updateMember(Member member);

    int updateSocialMember(Member member);

    int updateProfile(Member member);

    int removeProfilePicture(String memberId);

    int addDeliveryAddress(AddressBook addressBook);

    void setDefaultAddress(int addressId);

    List<AddressBook> getDeliveryAddress(int memNo);

    Member isMemberExists(String memberId, String password);

    String getEncryptedPassword(String memberId);

    void removeAccount(String memberId);

    AddressBook getDefaultAddress(int memNo);

    int checkDefaultAddress(int addressNo);

    int removeAddressBook(int addressNo);

    String uploadProfilePicture(MultipartFile file, String memberId) throws IOException;
}
