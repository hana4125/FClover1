package hello.fclover.service;



import hello.fclover.domain.Inquiry;
import hello.fclover.domain.Notice;
import hello.fclover.mybatis.mapper.InquiryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InquiryServiceImpl implements InquiryService {

    private final InquiryMapper dao;


    @Override
    public int ListCount() {
        return dao.ListCount();
    }

    @Override
    public List<Inquiry> BoardList(Integer page, int limit) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();

        int startrow=(page-1)*limit;
        map.put("start", startrow);
        map.put("limit", limit);

        return dao.BoardList(map);
    }



}
