package hello.fclover.service;

import hello.fclover.domain.AddressBook;
import hello.fclover.domain.Coupon;
import hello.fclover.domain.Member;
import hello.fclover.dto.CartDTO;
import hello.fclover.dto.WishDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MemberService {

    int signup(Member member);

    Long getmemberNo(String memberId);

    Member findMemberById(String memberId);

    String isMemberIdDuplicate(String memberId);

    String findMemberId(Member member);

    Member selectMemberResetPassword(Member member);

    int updateMember(Member member);

    int updateSocialMember(Member member);

    int updateProfile(Member member);

    int removeProfilePicture(String memberId);

    int addDeliveryAddress(AddressBook addressBook);

    void setDefaultAddress(int addressId);

    List<AddressBook> getDeliveryAddress(Long memberNo);

    Member isMemberExists(String memberId, String password);

    String getEncryptedPassword(String memberId);

    void removeAccount(String memberId);

    AddressBook getDefaultAddress(Long memberNo);

    int checkDefaultAddress(int addressNo);

    int removeAddressBook(int addressNo);

    String uploadProfilePicture(MultipartFile file, String memberId) throws IOException;

    List<WishDTO> getWishDTOList(Long memberNo);

    void createCoupon(String memberId);

    void removeWishList(Long wishNo, Long memberNo);

    void removeAllWishList(Long memberNo);

    List<CartDTO> getCartItems(Long memberNo);

    void removeCartItems(Long cartNo);

    List<Coupon> getActiveCouponsForUser(String memberId);

}
