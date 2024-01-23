package com.swyg.picketbackend.auth.dto.member.req;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostNicknameRequestDTO {

    @Schema(description = "닉네임 중복 체크",example = "닉네임")
    private String nickname;
}
