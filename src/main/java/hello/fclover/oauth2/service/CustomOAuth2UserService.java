package hello.fclover.oauth2.service;

import hello.fclover.domain.Member;
import hello.fclover.oauth2.dto.CustomOAuth2User;
import hello.fclover.oauth2.dto.GoogleResponse;
import hello.fclover.oauth2.dto.NaverResponse;
import hello.fclover.oauth2.dto.OAuth2Response;
import hello.fclover.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberService memberService;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("oAuth2User: {}", oAuth2User.getAttributes());

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;

        if (registrationId.equals("naver")) {

            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("google")) {

            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        String username = oAuth2Response.getProvider() + "_" + oAuth2Response.getProviderId();
        Member socialMember = memberService.findMemberById(username);

        if (socialMember == null) {
            Member member = new Member();

            member.setMemberId(username);
            member.setEmail(oAuth2Response.getEmail());
            member.setName(oAuth2Response.getName());
            member.setAuth("ROLE_MEMBER");

            log.info(member.toString());

            int result = memberService.signup(member);

            if (result == 1) {
                log.info("소셜 회원가입 성공");
            } else {
                log.info("소셜 회원가입 실패");
            }
        } else {
            log.info(socialMember.toString());
        }

        return new CustomOAuth2User(oAuth2Response);
    }
}
