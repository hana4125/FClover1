package hello.fclover.mybatis.mapper;

import hello.fclover.domain.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {

    int insert(Member member);

    Member isId(String id);

    int memberUpdate(Member member);
}
