package hello.fclover.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("로그인 성공 : LoginSuccessHandler");
        String url = request.getContextPath() + "/";

        String member_id = request.getParameter("member_id");
        String rememberMe = request.getParameter("remember-me");

        if ("on".equals(rememberMe)) {
            // 쿠키 생성
            Cookie cookie = new Cookie("rememberId", member_id);
            cookie.setMaxAge(7 * 24 * 60 * 60); // 쿠키 유효기간: 7일
            cookie.setPath("/");
            response.addCookie(cookie);
        } else {
            // 체크박스 해제 시 기존 쿠키 삭제
            Cookie cookie = new Cookie("rememberId", null);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        response.sendRedirect(url);
    }
}
