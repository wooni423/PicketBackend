package com.swyg.picketbackend.global.config;


import com.swyg.picketbackend.auth.jwt.JwtAccessDeniedHandler;
import com.swyg.picketbackend.auth.jwt.JwtAuthenticationEntryPoint;
import com.swyg.picketbackend.auth.jwt.JwtSecurityConfig;
import com.swyg.picketbackend.auth.jwt.TokenProvider;
import com.swyg.picketbackend.auth.oauth.OAuthFailureHandler;
import com.swyg.picketbackend.auth.oauth.OAuthSuccessHandler;
import com.swyg.picketbackend.auth.service.PrincipalOauthDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@Log4j2
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final CorsFilter corsFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final PrincipalOauthDetailService principalOauthDetailService;
    private final OAuthSuccessHandler oAuthSuccessHandler;
    private final OAuthFailureHandler oAuthFailureHandler;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("------------security configure-----------------");
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                // exception handling 할 때 우리가 만든 클래스를 추가
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler))

                .headers(headers ->
                        headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))

                // 시큐리티는 기본적으로 세션을 사용
                // 여기서는 세션을 사용하지 않기 때문에 세션 설정을 Stateless 로 설정

                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers("/auth/**",
                                        "/board/**",
                                        "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                                .requestMatchers("/board/myposts/**").authenticated() // 내 버킷목록 보기
                                .requestMatchers(HttpMethod.POST, "/board/**").authenticated() // 버킷 관련 등록
                                .requestMatchers(HttpMethod.DELETE, "/board/**").authenticated() // 버킷 관련 삭제
                                .requestMatchers(HttpMethod.PATCH, "/board/**").authenticated() // 버킷 관련 일부 수정
                                .requestMatchers(HttpMethod.PUT, "/board/**").authenticated() // 버킷 관련 전체 수정
                                .requestMatchers(HttpMethod.POST, "/member/profile/check-nickname").authenticated() // 프로필 닉네임 체크
                                .anyRequest().authenticated())

                .oauth2Login((oauth2Login) ->
                        oauth2Login
                                .userInfoEndpoint((userInfoEndpoint) ->
                                        userInfoEndpoint.userService(principalOauthDetailService))
                                .successHandler(oAuthSuccessHandler)
                                .failureHandler(oAuthFailureHandler)

                )
                .with(new JwtSecurityConfig(tokenProvider), customizer -> {
                });
        return http.build();
    }


}
