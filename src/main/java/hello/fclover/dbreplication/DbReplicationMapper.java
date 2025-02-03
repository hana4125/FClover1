/*
package hello.fclover.dbreplication;

import hello.fclover.domain.AddressBook;
import hello.fclover.domain.Member;
import hello.fclover.dto.WishDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DbReplicationMapper {

    @SetDataSource(dataSourceType = SetDataSource.DataSourceType.MASTER)
    int insertMember(Member member);

    @SetDataSource(dataSourceType = SetDataSource.DataSourceType.MASTER)
    Member selectMemberById(String id);

    @SetDataSource(dataSourceType = SetDataSource.DataSourceType.MASTER)
    int updateMember(Member member);

    @SetDataSource(dataSourceType = SetDataSource.DataSourceType.MASTER)
    int updateSocialMember(Member member);

    @SetDataSource(dataSourceType = SetDataSource.DataSourceType.MASTER)
    int updateProfile(Member member);

    @SetDataSource(dataSourceType = SetDataSource.DataSourceType.MASTER)
    int deleteProfilePicture(String memberId);

    @SetDataSource(dataSourceType = SetDataSource.DataSourceType.MASTER)
    int insertAddressBook(AddressBook addressBook);

    @SetDataSource(dataSourceType = SetDataSource.DataSourceType.MASTER)
    List<AddressBook> selectAddressBook(Long memberNo);

    @SetDataSource(dataSourceType = SetDataSource.DataSourceType.MASTER)
    Member selectMember(String memberId, String password);

    @SetDataSource(dataSourceType = SetDataSource.DataSourceType.MASTER)
    String selectMemberId(Member member);

    @SetDataSource(dataSourceType = SetDataSource.DataSourceType.MASTER)
    String selectPassword(String memberId);

    @SetDataSource(dataSourceType = SetDataSource.DataSourceType.MASTER)
    String isMemberIdDuplicate(String memberId);

    @SetDataSource(dataSourceType = SetDataSource.DataSourceType.MASTER)
    Member selectMemberResetPassword(Member member);

    @SetDataSource(dataSourceType = SetDataSource.DataSourceType.MASTER)
    void deleteMember(String memberId);

    @SetDataSource(dataSourceType = SetDataSource.DataSourceType.MASTER)
    Long selectMemberNo(String memberId);

    @SetDataSource(dataSourceType = SetDataSource.DataSourceType.MASTER)
    AddressBook selectDefaultAddress(Long memberNo);

    @SetDataSource(dataSourceType = SetDataSource.DataSourceType.MASTER)
    int updateDefaultAddress(int addressId);

    @SetDataSource(dataSourceType = SetDataSource.DataSourceType.MASTER)
    int selectIsDefault(int addressNo);

    @SetDataSource(dataSourceType = SetDataSource.DataSourceType.MASTER)
    int deleteAddressBook(int addressNo);

    @SetDataSource(dataSourceType = SetDataSource.DataSourceType.MASTER)
    List<WishDTO> selectWishList(Long memberNo);
}
*/
