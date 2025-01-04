package hello.fclover.mybatis.mapper;

import hello.fclover.domain.AddressBook;
import hello.fclover.domain.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberMapper {

    int insert(Member member);

    Member isId(String id);

    int updateMember(Member member);

    int insertAddressBook(AddressBook addressBook);

    List<AddressBook> selectAddressBook(int memNum);

    Member selectMember(String memberId, String password);

    String selectPassword(String memberId);

    void deleteMember(String memberId);

    int selectMemNum(String memberId);

    AddressBook selectDefaultAddress(int memNum);

    int updateDefaultAddress(int addressId);

    int selectIsDefault(int addressNum);

    int deleteAddressBook(int addressNum);
}
