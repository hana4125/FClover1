package hello.fclover.mybatis.mapper;

import hello.fclover.domain.Delivery;
import hello.fclover.domain.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

import java.util.List;

import java.util.List;

import java.util.List;

@Mapper
public interface MemberMapper {

    int insert(Member member);

    Member isId(String id);

    int memberUpdate(Member member);

    int getListCount();

    List<Member> getBoardList(HashMap<String, Integer> map);

    int insertDeliveryAddress(Delivery delivery);

    List<Delivery> selectDeliveryAddress(String member_id);

    Member selectMember(String member_id, String password);

    String selectPassword(String member_id);

    void deleteMember(String member_id);

    void setReadCountUpdate(int num);

    Member getDetail(int num);


}
