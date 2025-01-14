package hello.fclover.service;



import hello.fclover.domain.Inquiry;

import java.util.List;

public interface InquiryService {


    int ListCount();

    List<Inquiry> BoardList(Integer page, int limit);
}
