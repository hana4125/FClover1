package hello.fclover.security;

import hello.fclover.domain.Member;
import hello.fclover.domain.Seller;
import hello.fclover.mybatis.mapper.MemberMapper;
import hello.fclover.mybatis.mapper.SellerMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberMapper memberDao;
    private final SellerMapper sellerDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String userType = request.getParameter("userType");

        log.info("username = {}", username);
        log.info("userType = {}", userType);

        Object users = null;

        if (userType.equals("member")) {
            users = memberDao.selectMemberById(username);
        } else if (userType.equals("seller")) {
            users = sellerDao.selectSellerById(username);
        }

        if (users == null) {
            log.info("username {} not found", username);
            throw new UsernameNotFoundException(username);
        }

        Collection<SimpleGrantedAuthority> roles = new ArrayList<>();

        if (users instanceof Member) {
            Member member = (Member) users;
            roles.add(new SimpleGrantedAuthority(member.getAuth()));
            log.info("roles = {}", roles);
            UserDetails user = new User(username, member.getPassword(), roles);
            return user;
        } else {
            Seller seller = (Seller) users;
            roles.add(new SimpleGrantedAuthority("ROLE_SELLER"));
            UserDetails user = new User(username, seller.getPassword(), roles);
            return user;
        }
    }
}
