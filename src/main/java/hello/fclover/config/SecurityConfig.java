package hello.fclover.config;

import hello.fclover.oauth2.service.CustomOAuth2UserService;
import hello.fclover.security.LoginFailHandler;
import hello.fclover.security.LoginSuccessHandler;
import hello.fclover.security.SellerLoginFailHandler;
import hello.fclover.security.SellerLoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final LoginFailHandler loginFailHandler;
    private final LoginSuccessHandler loginSuccessHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

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
    public SecurityFilterChain adminFilterChain(HttpSecurity http, AdminLoginSuccessHandler adminLoginSuccessHandler, AdminLoginFailHandler adminLoginFailHandler) throws Exception {

        http
                .securityMatcher("/bo/**")
                .formLogin((formLogin) -> formLogin
                        .loginPage("/bo/login")
                        .loginProcessingUrl("/bo/loginProcess")
                        .usernameParameter("memberId")
                        .passwordParameter("password")
                        .successHandler(adminLoginSuccessHandler)
                        .failureHandler(adminLoginFailHandler))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/bo/login", "bo/signup", "/bo/signupProcess").permitAll()
                        .requestMatchers("/bo/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .logout((lo) -> lo.logoutUrl("/bo/logout")
                        .logoutRequestMatcher(new AntPathRequestMatcher("/bo/logout", "GET"))
                        .logoutSuccessUrl("/bo/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );
        return http.build();
    }

    @Bean
    public SecurityFilterChain memberFilterChain(HttpSecurity http) throws Exception {

        http
                .formLogin((formLogin) -> formLogin.loginPage("/member/login")
                        .loginProcessingUrl("/member/loginProcess")
                        .usernameParameter("memberId")
                        .passwordParameter("password")
                        .successHandler(loginSuccessHandler)
                        .failureHandler(loginFailHandler))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/member/main", "/member/login", "/member/signup", "/member/signupProcess",
                                "/member/find-id", "/member/find-id-ok", "/member/send-code-id", "member/send-code-password",
                                "/member/reset-password","/member/reset-password-ok", "/inquiry/**", "/member/category/**","/member/goodsDetail/**").permitAll()
                        .requestMatchers("/inquiry/notice/write").hasAnyAuthority("ROLE_ADMIN","ROLE_MEMBER")
                        .requestMatchers("/inquiry/question/**").hasAnyRole("ADMIN","MEMBER")
                        .requestMatchers("/member/**").hasRole("MEMBER")
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/oauth2/authorization/{registrationId}")
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(customOAuth2UserService))
                        .defaultSuccessUrl("/member/main", true)
                )
                .logout((lo) -> lo.logoutUrl("/member/logout")
                        .logoutSuccessUrl("/member/main")
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
