//package hello.fclover.service.kakaoMessage;
//
//import org.json.JSONObject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//
//@Service
//public class AuthService extends HttpCallService {
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    private static final String AUTH_URL = "https://kauth.kakao.com/oauth/token";
//
//    public static String authToken;
//
//    public boolean getKakaoAuthToken(String code)  {
//        HttpHeaders header = new HttpHeaders();
//        String accessToken = "";
//        String refrashToken = "";
//        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
//
//        header.set("Content-Type", APP_TYPE_URL_ENCODED);
//
//        parameters.add("code", code);
//        parameters.add("grant_type", "authorization_code");
//        parameters.add("client_id", "aaca1175b697bf769b71b3e8d27a8fb3"); //REST API 키값
//        parameters.add("redirect_url", "http://localhost:5000"); //
//        parameters.add("client_secret", "eDXpUpTrBUgogtVTl3mS7O1644nbgslY"); // 카카오 Client Secret 코드
//
//        HttpEntity<?> requestEntity = httpClientEntity(header, parameters);
//
//        ResponseEntity<String> response = httpRequest(AUTH_URL, HttpMethod.POST, requestEntity);
//        JSONObject jsonData = new JSONObject(response.getBody());
//        accessToken = jsonData.get("access_token").toString();
//        refrashToken = jsonData.get("refresh_token").toString();
//        if(accessToken.isEmpty() || refrashToken.isEmpty()) {
//            logger.debug("토큰발급에 실패했습니다.");
//            return false;
//        }else {
//            authToken = accessToken;
//            return true;
//        }
//    }
//}
