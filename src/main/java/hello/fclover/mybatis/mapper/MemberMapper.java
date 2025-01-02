package hello.fclover.mybatis.mapper;

import hello.fclover.domain.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface MemberMapper {

    int insert(Member member);

    Member isId(String id);

    int memberUpdate(Member member);

    int getListCount();

    List<Member> getBoardList(HashMap<String, Integer> map);
}
