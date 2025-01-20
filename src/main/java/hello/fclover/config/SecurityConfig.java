package hello.fclover.config;

import hello.fclover.oauth2.service.CustomOAuth2UserService;
import hello.fclover.security.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final LoginFailHandler loginFailHandler;
    private final LoginSuccessHandler loginSuccessHandler;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain sellerFilterChain(HttpSecurity http, SellerLoginSuccessHandler sellerLoginSuccessHandler, SellerLoginFailHandler sellerLoginFailHandler) throws Exception {

        http
                .securityMatcher("/seller/**")
                .formLogin(formLogin -> formLogin
                        .loginPage("/seller/login")
                        .loginProcessingUrl("/seller/loginProcess")
                        .usernameParameter("sellerId")
                        .passwordParameter("password")
                        .successHandler(sellerLoginSuccessHandler)
                        .failureHandler(sellerLoginFailHandler))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/seller/login", "/seller/signup", "/seller/signupProcess").permitAll()
                        .requestMatchers("/seller/**").hasRole("SELLER"))
                .logout((lo) -> lo.logoutUrl("/seller/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"));

        return http.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                //.securityMatcher("/inquiry/**", "/member/**")
                .formLogin((formLogin) -> formLogin.loginPage("/member/login")
                        .loginProcessingUrl("/member/loginProcess")
                        .usernameParameter("memberId")
                        .passwordParameter("password")
                        .successHandler(loginSuccessHandler)
                        .failureHandler(loginFailHandler))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/member/main", "/member/login", "/member/signup", "/member/signupProcess",
                                "/member/find-id", "/member/find-id-ok", "/member/send-code-id", "member/send-code-password",
                                "/member/reset-password","/member/reset-password-ok", "/inquiry/**", "/member/category/**", "/member/goodsDetail/**").permitAll()
                        .requestMatchers("/", "/member/main", "/member/login", "/member/signup",
                                "/member/signupProcess", "/member/find-id", "/member/find-id-ok",
                                "/member/reset-password", "/inquiry/**", "/member/category/**").permitAll()
                        .requestMatchers("/inquiry/notice/write").hasAnyAuthority("ROLE_ADMIN","ROLE_MEMBER")
                        .requestMatchers("/inquiry/question/**").hasAnyRole("ADMIN","MEMBER")
                        .requestMatchers( "/","/member/main", "/member/login", "/member/signup", "/member/signupProcess",
                               "/inquiry/**").permitAll()
                        .requestMatchers("/member/**").hasRole("MEMBER")
                        .anyRequest().authenticated()

                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/oauth2/authorization/{registrationId}")
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(customOAuth2UserService))
                        .defaultSuccessUrl("/", true)
                )
                .logout((lo) -> lo.logoutUrl("/member/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}
