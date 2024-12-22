package hello.fclover.controller;

import hello.fclover.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;


@Slf4j
@Controller
@RequestMapping(value="/member")
public class MemberController {
    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    private final MemberService memberService;

    MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/login")
    public ModelAndView login(ModelAndView mv, @CookieValue(value="remember-me", required = false) Cookie readCookie,
                              HttpSession session, Principal userPrincipal) {
        if(readCookie != null) {
            logger.info("저장된 아이디 :" + userPrincipal.getName());
            mv.setViewName("redirect:/bo/main");
        }else{
            mv.setViewName("user/userLogin");
            mv.addObject("message",session.getAttribute("loginfail"));
            session.removeAttribute("loginfail");
        }
        return mv;

    }

}
