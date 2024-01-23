package com.swyg.picketbackend.auth.dto.member.req;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PutPasswordDTO {

        @Schema(description = "비밀 번호 찾기 메일 전송을 위한 가입 이메일",example = "test@naver.com")
        String email;
}
