package hello.fclover.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 제공자 정보 가져오기 (google, naver, kakao 등)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // 사용자 정보를 표준화된 CustomOAuth2User로 변환
        return new CustomOAuth2User(oAuth2User, registrationId);
    }
}
