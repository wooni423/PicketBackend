package com.swyg.picketbackend.auth.dto.auth.req;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {

    @Email(message = "이메일 형식에 맞지 않습니다.")
    @Schema(description = "로그인 이메일",example = "test@naver.com")
    String email;


    @Schema(description = "로그인 비밀번호",example = "password")
    String password;


    // email,password 을 기반으로 한 권한 추출
    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}
