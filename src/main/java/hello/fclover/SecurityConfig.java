package hello.fclover;

import hello.fclover.security.CustomAccessDeniedHandler;
import hello.fclover.security.CustomUserDetailService;
import hello.fclover.security.LoginFailHandler;
import hello.fclover.security.LoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@EnableWebSecurity //SPring Security 활성화 시켜 모든 요청이 스프링 시큨리티의 제어를 받도록 합니다.
@Configuration
public class SecurityConfig {

    private LoginFailHandler loginFailHandler;
    private LoginSuccessHandler loginSuccessHandler;
    private DataSource dataSource;
    private CustomUserDetailService customUserDetailService;
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    /*
    @Bean 는 개발자가 직접 제어가 불가능한 외부 라이브러리등을 Bean으로 만들려할 때 사용됩니다.
    @Configuration 붙어 있는 클래스 안에서 사용합니댜ㅏ.

     */

    public SecurityConfig(LoginFailHandler loginFailHandler, LoginSuccessHandler loginSuccessHandler, DataSource dataSource, CustomUserDetailService customUserDetailService, CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.loginFailHandler = loginFailHandler;
        this.loginSuccessHandler = loginSuccessHandler;
        this.dataSource = dataSource;
        this.customUserDetailService = customUserDetailService;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        /*
            1) loginPage("/member/login") : 스프링에서 제공되는 login페이지가 아닌 내가 만든 페이지로 이동합니다.
            2) loginProcessingUrl("/member/loginProcess") : 로그인을 처리하기 위한 url을 매개변수에 넣습니다.
                                                (form의 action에 해당하는 경로 적으세요. method는 post 방식이어ㅑ 합니다.)
            3) usernameParameter("id") : 사용자 계정명을 어떠한 파라미터명으로 받을 것인지 설정합니다.
                                        form의 input태그의 사용자 계정 name과 동일하게 작성합니다.
                                        우리가 사용하는 아이디가 스프링 시큐리티에서는 USERName으로 사용됩니다.
            4) passwordparameter("password") : form의 input태그 패스워드 Nmae 과 동일하게 작성합ㄴ디ㅏ.
            5) successhandler(AuthenicationSUccessHandler successHandler) : 로그인 성공시 처리할 핸들러를 매개변수로 사용합니다.
         */
//         http.csrf((csrf)->csrf.disable());

        //내가 만든 로그인 페이지로 이동합ㄴ디ㅏ.
        http.formLogin((formLogin)->formLogin.loginPage("/member/login")
                .loginProcessingUrl("/member/loginProcess")
                .usernameParameter("id")
                .passwordParameter("password")
                        .failureHandler(loginFailHandler)
                .successHandler(loginSuccessHandler)
                );


        //로그아웃
        /*
        1. http.logout() : Spring Security에서 제공하는 HttpSecurity객체의 logout()메서드를  호출하여 로그아웃 설정을 시작
        2. logoutsuccessUrl("/member/login") : 로그아웃 성공후에 사용자를 이동시킬 url을 지정합니다.
        3. logoutUrl("/member/logout") : 로그아웃을 수행할 url을 지정합니다.
                                        true로 설정되어 있으므로 로그아웃 시 http세션이 무효화됩니다.
        4. invalidateHttpSession(true) : HTTP 세션을 무효화할지 여부를 지정합니다.
                                            true로 설정되어 있으므로 로그아웃 시 HTTP세션이 무효화됩니다.
        5. deleteCookies("remember-me","JESSION_ID") : 로그아웃 시 삭제할 쿠키를 지정합니다.
                                                        "remember-me"와 "JSESSION_ID"라는 두개의 쿠키를 삭제하도록 설정되어있습니다.
         */


        http.logout((lo)->lo.logoutSuccessUrl("/member/login")
                                        .logoutUrl("/member/logout")
                .invalidateHttpSession(true)
                .deleteCookies("remember-me","JSESSION_ID"));



//
        /*
            로그인 유지하기 기능
            1) 로그인 폼에서 체크박스의 nameㄴ이 "remember-me"를 선택하면 기능이 작동합니다.
            (2) rememberMeParameter("remember-me") : 체크박스의 name 속성의 값을 매개변수로 작성합니다.
            예) <input type="checkbox" name="remember-me"
            (3) userDetailsService(customUserDetailsService) :
        사용자 인증과 관련된 작업을 수행할 때 사용할 사용자 상세 정보 서비스를 설정합니다.
            (4) tokenValiditySeconds(2419200) :
        초 단위로 2419200조를 설정했으므로, 약 28일 동안 "remember-me" 토큰이 유효하게 됩니다.
            (5) tokenRepository(tokenRepository() : 데이터 베이스에 토큰을 저장합니다.
         */
        http.rememberMe((me)->me.rememberMeParameter("remember-me")
                .userDetailsService(customUserDetailService)
                .tokenValiditySeconds(2419200)
                .tokenRepository(tokenRepository()));

        http.exceptionHandling((ex)->ex.accessDeniedHandler(customAccessDeniedHandler));


        /*
        * HTTP 요청에 대한 권한 부여 규칙을 설정합니다.
        (1) http.authorizeHttpRequests(): HTTP 요청에 대한 권한을 설정하기 위한 메서드를 호출합니다.
        (2) requestMatchers(): Spring Security에서 HTTP 요청을 매칭하는 패턴을 정의하는 부분입니다
        (3) hasAuthority() 메서드는 Spring Security에서 사용자가 특정 권한을 갖고 있는지를 확인하는 메서드입니다.
        hasAuthority("ROLE_ADMIN")는 "ROLE_ADMIN"의 권한을 가지고 있는지 확인합니다.
hyst AHor Ll RPE CREAER 만트 타 금지 모두가 해당 요청에 접근할 수 있다는 것을 의미합니다.
(4). requestMatchers("/**")는 모든 요청에 대해 매치하도록 설정하는 것을 의미합니다.
(5) permitAll(): 해당 요청에 대한 접근을 모든 사용자에게 허용합니다.
이는 권한 부여 규칙 중에서 가장 넓은 범위로, 모든 사용자가 접근할 수 있습니다.
         */

        http.authorizeHttpRequests(
                (au)->au.requestMatchers("/member/list","/member/info","/member/delete").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers("/member/update").hasAnyAuthority("ROLE_ADMIN","ROLE_MEMBER")
                        .requestMatchers("/board/**","/comment/**")
                        .hasAnyAuthority("ROLE_ADMIN","ROLE_MEMBER")
                        .requestMatchers("/**").permitAll()
        );

        return http.build();
    }

    @Bean
    public PersistentTokenRepository tokenRepository() {
        //PersistentTokenRepository의 구현체인 JdbcTokenRepositoryImpl클래스 사용합니다.
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource); //import javax.sql.Datasource
        return jdbcTokenRepository;
    }

    /*
    1. 스프링 시큐리티란 자바 서버 개발을 위해 피룡로 한 인증,권한 부여 및 기타 보안 기능을 제공하는 프레임워크(클래스와 인터페이스 모임)입니다.
    2. BCryptPasswordEncoder는 스프링 시큐리티 프레임워크에서 제공하는 클래스 중 하나로
        BCryptPasswordENcoder는 스프링 시큐맅티 프레임워크에서 제공하는 클래스 중 하나로
           BCrypt해싱 함수(BCrypt hashing function - 암호화 해시 함수)를 사용해서 비밀번호를 인코딩해주는 메서드와
           데이터베이스에 저장된 비밀번호와 일치하는지를 알려주는 메서드를 가진 클래스입니다.

      3. BCryptPasswordENcoder란 PasswordENcoder 인터페이스를 구현한 클래스입니다.
     */

    @Bean
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }
}
