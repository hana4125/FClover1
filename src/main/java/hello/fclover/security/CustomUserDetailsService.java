package hello.fclover.security;

import hello.fclover.domain.Member;
import hello.fclover.mybatis.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberMapper dao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member users = dao.isId(username);

        if (users == null) {
            log.info("username {} not found", username);
            throw new UsernameNotFoundException(username);
        }

        Collection<SimpleGrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(users.getAuth()));

        UserDetails user = new User(username, users.getPassword(), roles);

        return user;
    }
}
