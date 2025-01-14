package hello.fclover.mybatis.mapper;


import hello.fclover.domain.Inquiry;
import org.apache.ibatis.annotations.Mapper;
import java.util.HashMap;
import java.util.List;


@Mapper
public interface InquiryMapper {

    int ListCount();

    List<Inquiry> BoardList(HashMap<String, Integer> map);


}
