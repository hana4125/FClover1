package hello.fclover;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Test {

    @GetMapping("/test")
    public String test() {
        return "/seller/sellerMypage2";
    }

    @GetMapping("/test2")
    public String test2() {
        return "/fragment/mypage";
    }
}
