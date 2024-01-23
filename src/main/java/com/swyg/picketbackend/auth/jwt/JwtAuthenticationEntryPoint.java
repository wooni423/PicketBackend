package com.swyg.picketbackend.auth.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

// AuthenticationEntryPoint => Spring Security에서 인증에 실패했을 때 사용자를 인증 페이지로 리디렉션하거나, 에러 메시지를 반환하는 등의 특별한 동작을 정의하는 인터페이스
// 사용자 정보가 잘못되거나, 토큰이 유효하지 않은 경우에 대비하기 위한 클래스
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // 유효한 자격증명을 제공하지 않고 접근하려 할때 401
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED); // TODO 커스텀 예외처리 추가
    }
}

