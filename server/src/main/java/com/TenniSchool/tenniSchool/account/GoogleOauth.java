package com.TenniSchool.tenniSchool.account;

import com.TenniSchool.tenniSchool.domain.GoogleUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/*
@Com이거 쓰면 BeanConfiguration파일에 bean을 따로 등록하지않아도 사용가능.
빈등록 자체를 빈클래스 자체에다가 할 수있다. 타입기반의 자동주입 어노테이션으로 @Autowired, @Resource와 비슷한 기능 수행
 */
@Component
@RequiredArgsConstructor
public class GoogleOauth implements SocialOauth{

    //application.yaml에서 value annotation을 통해서 값을 받아온다.
    @Value("${spring.OAuth2.google.url}")
    private String GOOGLE_SNS_LOGIN_URL;
    @Value("${spring.OAuth2.google.client-id}")
    private String GOOGLE_SNS_CLIENT_ID;
    @Value("${spring.OAuth2.google.callback-url}")
    private String GOOGLE_SNS_CALLBACK_URL;
    @Value("${spring.OAuth2.google.client-secret}")
    private String GOOGLE_SNS_CLIENT_SECRET;
    @Value("${spring.OAuth2.google.scope}")
    private String GOOGLE_DATA_ACCESS_SCOPE;

    private final ObjectMapper objectMapper;
    @Override
    public String getOauthRedirectURL() {

        Map<String,Object> params = new HashMap<>();
        params.put("scope", GOOGLE_DATA_ACCESS_SCOPE);
        params.put("response_type","code");
        params.put("client_id",GOOGLE_SNS_CLIENT_ID);
        params.put("redirect_uri",GOOGLE_SNS_CALLBACK_URL);

        //parameter를 형식에 맞춰 구성해주는 함수
        String parameterString=params.entrySet().stream()
                .map(x->x.getKey()+"="+x.getValue())
                .collect(Collectors.joining("&"));

        String redirectURL=GOOGLE_SNS_LOGIN_URL+"?"+parameterString;

        System.out.println("redirectURL ="+redirectURL);

        return redirectURL;
        /*
        https://accounts.google.com/o/oauth2/v2/auth?scope=profile&response_type=code
        &client_id = "할당받은 id"&redirect_uri="access token 처리")
        로 Redirect URL을 생성하는 로직을 구성
        */
    }

    /*
    resttemplate : spring부트에서는 다른 서버의 api endpoint를 호출할 때 resttemplate를 많이 사용한다.
    http서버와의 통신을 단순화하고 restful 원칙을 지킬수있다. (json,xml을 쉽게 응답 받는다.)
     */
    public ResponseEntity<String> requestAccessToken(String code){
        String GOOGLE_TOKEN_REQUEST_URL="https://oauth2.googleapis.com/token";
        RestTemplate restTemplate= new RestTemplate();
        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", GOOGLE_SNS_CLIENT_ID);
        params.put("client_secret", GOOGLE_SNS_CLIENT_SECRET);
        params.put("redirect_uri", GOOGLE_SNS_CALLBACK_URL);
        params.put("grant_type", "authorization_code");

        ResponseEntity<String> responseEntity=restTemplate.postForEntity(GOOGLE_TOKEN_REQUEST_URL,
                params,String.class);

        if(responseEntity.getStatusCode()== HttpStatus.OK){
            return responseEntity;
        }
        return null;
    }

    //다시 구글로 액세스 토큰을 보내서 구글 사용자 정보를 받아온다.
    //역시 구글 api문서를 참고해 request 형식을 올바르게 구성해야한다.
    //이전에 계속 파라미터로 request를 보냇으니, 이번엔 헤더에 access token을 담는 방식으로 구현해본다.
    public ResponseEntity<String> requestUserInfo(GoogleOAuthToken oAuthToken){
        String GOOGLE_USERINFO_REQUEST_URL="https://www.googleapis.com/oauth2/v1/userinfo";

        //header에 accessToken을 담는다.
        //HttpHeaders : Header에 원하는 방식으로 key-value값을 설정해서 보낼 수 있는 객체이다.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+oAuthToken.getAccess_token());

        //HttpEntity를 하나 생성해 헤더를 담아서 restTemplate으로 구글과 통신하게 된다.
        //HttpEntity<T>:HttpEntity는 http 요청/응답에 해당하는 HTTPHeader와 HTTPBody를 포함하는 객체이다.
        //따라서 HttpEntity를 생성할 때 header를 생성자 파라미터로 추가할 수 있다.
        //ResponseEntity:일반적인 api는 반환하는 리소스에 value만 있지 않으며, 상태코드, 응답메세지등이 포함될 수 있다.
        //따라서 responseEntity는 client가 보내는 여러가지 응답내용을 규격에 맞게 한번 감싸주는 역할을 한다.
        //같은 역할로는 @ResponseBody 어노테이션이있다. Httpentity를 상속받고 있는 클래스이다.

        //MultiValueMap : 키 하나에 여러개의 value를 가지게 해주는 map. key-value각각 하나씩이 쌍으로 이뤄지는 map과는 다르다.
        //중요한 특징 중 하나는 restTemplate의 exchange메서드의 세번째 파라미터 request에는 Hashmap을 지원하지 않아 무조건 MultiValueMap을 사용해야한다는 것이다.
        //HTTP Request시에 URL을 전송할 때 name="jennie"&name="lisa"&name="peter"와 같이 같은 파라미터에 여러가지 값이 전송되는 사례가 있기 때문.
        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(GOOGLE_USERINFO_REQUEST_URL, HttpMethod.GET,request,String.class);
        System.out.println("response.getBody() = " + response.getBody());
        System.out.println("유저정보");
        return response;

    }
    //마지막으로, 이 구글 유저 정보가 담긴 Json문자열을 파싱하여 googleuser객체에 담아주면 된다.
    public GoogleUser getUserInfo(ResponseEntity<String> userInfoRes)throws JsonProcessingException{
        GoogleUser googleUser=objectMapper.readValue(userInfoRes.getBody(), GoogleUser.class);
        System.out.println("구글유저: "+googleUser);
        return googleUser;
    }
    //responseEntity에 담긴 jsonString을 역직렬화해 자바객체에 담는다.
    public GoogleOAuthToken getAccessToken(ResponseEntity<String> response)throws JsonProcessingException{
        System.out.println("response.getBody() = "+response.getBody());
        GoogleOAuthToken googleOAuthToken = objectMapper.readValue(response.getBody(),GoogleOAuthToken.class);
        return googleOAuthToken;
    }
}
