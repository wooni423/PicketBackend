package com.swyg.picketbackend.auth.dto.member.res;


import com.swyg.picketbackend.auth.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatchMemberResponseDTO {

    @Schema(description = "수정된 닉네임", example = "test@naver.com")
    private String nickname; //  수정된 닉네임

    @Schema(description = "수정된 이미지 주소", example = "이미지 주소")
    private String imageUrl; //  수정된 이미지 주소

    // dto -> entity
    public static PatchMemberResponseDTO of(Member member) {
        return PatchMemberResponseDTO.builder()
                .nickname(member.getNickname())
                .imageUrl(member.getImageUrl())
                .build();
    }
}
