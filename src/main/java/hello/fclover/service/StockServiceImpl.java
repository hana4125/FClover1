package hello.fclover.service;

import hello.fclover.mybatis.mapper.StockMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class StockServiceImpl implements StockService {


    private final StockMapper dao;

    public StockServiceImpl(StockMapper dao){
        this.dao = dao;
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void decrease(int id, int quantity) {
           dao.decrease(quantity);
    }

}


