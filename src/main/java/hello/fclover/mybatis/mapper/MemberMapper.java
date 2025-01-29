package hello.fclover.mybatis.mapper;

import hello.fclover.domain.AddressBook;
import hello.fclover.domain.Member;
import hello.fclover.domain.Notice;
import hello.fclover.dto.WishDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface MemberMapper {

    int insertMember(Member member);

    Member selectMemberById(String id);

    int updateMember(Member member);

    int updateSocialMember(Member member);

    int updateProfile(Member member);

    int deleteProfilePicture(String memberId);

    int insertAddressBook(AddressBook addressBook);

    List<AddressBook> selectAddressBook(Long memberNo);

    Member selectMember(String memberId, String password);

    String selectMemberId(Member member);

    String selectPassword(String memberId);

    String isMemberIdDuplicate(String memberId);

    Member selectMemberResetPassword(Member member);

    void deleteMember(String memberId);

    Long selectMemberNo(String memberId);

    AddressBook selectDefaultAddress(Long memberNo);

    int updateDefaultAddress(int addressId);

    int selectIsDefault(int addressNo);

    int deleteAddressBook(int addressNo);

    List<WishDTO> selectWishList(Long memberNo);
}
