package com.swyg.picketbackend.auth.oauth;


import com.swyg.picketbackend.auth.dto.auth.req.LoginDTO;
import com.swyg.picketbackend.auth.service.AuthService;
import com.swyg.picketbackend.auth.util.PrincipalDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;


@Component
@RequiredArgsConstructor
@Slf4j
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler { // oauth 소셜 로그인 성공후 성공 Handler

    private final AuthService authService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        String email = principalDetails.getName();

        String password = "google";

//        SecurityContext securityContext = SecurityContextHolder.getContext();
//
//        securityContext.setAuthentication(null);

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail(email);
        loginDTO.setPassword(password);

        ResponseEntity<String> tokenResponse = sendLoginRequest(loginDTO);

        String accessToken = extractAccessToken(tokenResponse);

        log.info(tokenResponse.getBody());
        log.info("Access Token: {}", accessToken);

        response.addHeader("Authorization", "Bearer " + accessToken);

        response.sendRedirect("https://picket.store/auth/social-success");
    }

    private ResponseEntity<String> sendLoginRequest(LoginDTO loginDTO) { // 로그인 요청
        String loginUrl = "https://picket.store/auth/login";

        // Set up headers
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");

        // Set up the request entity
        HttpEntity<LoginDTO> requestEntity = new HttpEntity<>(loginDTO, headers);

        // Send the request
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForEntity(loginUrl, requestEntity, String.class);
    }

    private String extractAccessToken(ResponseEntity<String> tokenResponse) {
        // 여기서는 간단하게 JSON 문자열을 파싱하는 예제를 제시합니다.
        // 만약 실제로는 JSON 라이브러리를 사용하는 것이 좋습니다.
        String jsonResponse = tokenResponse.getBody();


        assert jsonResponse != null;
        int startIndex = jsonResponse.indexOf("\"accessToken\":\"") + "\"accessToken\":\"".length();
        int endIndex = jsonResponse.indexOf("\"", startIndex);

        // accessToken 추출
        if (startIndex != -1 && endIndex != -1) {
            return jsonResponse.substring(startIndex, endIndex);
        } else {
            // 추출 실패 시 처리 (예: 빈 문자열 또는 예외 throw)
            throw new RuntimeException("Failed to extract accessToken from JSON response.");
        }

    }
}