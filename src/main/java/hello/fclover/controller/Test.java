package hello.fclover.controller;

import hello.fclover.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@RestController
public class Test {

    @GetMapping("/test")
    public String test(Principal principal) {
        String id = principal.getName();
        log.info("id={}", id);
        return "ok";
    }
}
