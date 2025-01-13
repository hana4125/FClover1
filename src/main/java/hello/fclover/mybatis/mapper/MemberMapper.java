package hello.fclover.mybatis.mapper;

import hello.fclover.domain.AddressBook;
import hello.fclover.domain.Member;
import org.apache.ibatis.annotations.Mapper;

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

    List<AddressBook> selectAddressBook(int memberNo);

    Member selectMember(String memberId, String password);

    String selectMemberId(Member member);

    String selectPassword(String memberId);

    Integer selectMemberResetPassword(Member member);

    void deleteMember(String memberId);

    int selectmemberNo(String memberId);

    AddressBook selectDefaultAddress(int memberNo);

    int updateDefaultAddress(int addressId);

    int selectIsDefault(int addressNo);

    int deleteAddressBook(int addressNo);
}
