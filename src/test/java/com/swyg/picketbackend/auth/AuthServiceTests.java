package com.swyg.picketbackend.auth;
import com.swyg.picketbackend.auth.service.AuthService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Log4j2
@SpringBootTest
public class AuthServiceTests {


    @Autowired
    private AuthService authService;

    @Test
    @DisplayName("이메일 중복 체크 서비스 테스트")
    public void  checkEmail() {
        // given
        String email = "jiwoong42@naver.com";

        // when
        authService.checkEmail(email);

        // then


    }

    @Test
    @DisplayName("회원 가입 닉네임 설정 서비스 테스트")
    public void nickNameAddTests() {
        // given
        String email = "test1@naver.com";
        String nickname = "wooni";
        // when

        // then

    }
}
