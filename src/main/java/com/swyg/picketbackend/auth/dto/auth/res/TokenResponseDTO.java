package com.swyg.picketbackend.auth.dto.auth.res;

import lombok.*;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenResponseDTO {

    private Long memberId;

    private String grantType;

    private String nickname;

    private String profileUrl;

    private String accessToken;

    private String refreshToken;

    private Long accessTokenExpiresIn;


    // 닉네임 get 메서드
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    // 프로필 이미지 url get 메서드
    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }
}

