package hello.fclover.service;

import hello.fclover.domain.AddressBook;
import hello.fclover.domain.Member;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

public interface MemberService {

    int signup(Member member);

    int getMemNum(String memberId);

    Member findMemberById(String memberId);

    int updateMember(Member member);

    int updateSocialMember(Member member);

    int updateProfile(Member member);

    int removeProfilePicture(String memberId);

    int addDeliveryAddress(AddressBook addressBook);

    void setDefaultAddress(int addressId);

    List<AddressBook> getDeliveryAddress(int memNum);

    Member isMemberExists(String memberId, String password);

    String getEncryptedPassword(String memberId);

    void removeAccount(String memberId);

    AddressBook getDefaultAddress(int memNum);

    int checkDefaultAddress(int addressNum);

    int removeAddressBook(int addressNum);

    String uploadProfilePicture(MultipartFile file, String memberId) throws IOException;
}
