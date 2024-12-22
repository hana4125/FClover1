package hello.fclover.security;

import hello.fclover.domain.Member;
import hello.fclover.mybatis.mapper.MemberMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class CustomUserDetailService implements UserDetailsService {


    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailService.class);


    private MemberMapper dao;
    public CustomUserDetailService(MemberMapper dao) {
        this.dao = dao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            logger.info("username은 로그인시 입력한 값 : " + username);
            Member users = dao.isId(username);

            if(users == null) {
                logger.info("username" + username + " not found");
                throw new UsernameNotFoundException("username" + username + " not found");
            }


            //GrantAuthority : 인증 개체에 부여된 권한을 나타내기 위한 인터페이스로 이를 구현한 구현체는 생성자에 권한을 문자열로 넣어주면 됩니다.
        //SimpleGrantAuthority : GrantAuthority의 구현체입니다.
            Collection<SimpleGrantedAuthority> roles = new ArrayList<SimpleGrantedAuthority>();
            roles.add(new SimpleGrantedAuthority(users.getAuth()));

            UserDetails user  = new User(username,users.getPassword(),roles);
        return user;
    }
}
