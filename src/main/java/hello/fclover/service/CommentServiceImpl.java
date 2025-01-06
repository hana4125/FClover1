package hello.fclover.service;

import hello.fclover.domain.Delivery;
import hello.fclover.domain.Member;
import hello.fclover.mybatis.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

//@Service
//@RequiredArgsConstructor
//public class CommentServiceImpl implements CommentService {
//    private final CommetMapper dao;
//
//    @Override
//    public int getListCount(int board_num) {
//        return 0;
//    }
//}
