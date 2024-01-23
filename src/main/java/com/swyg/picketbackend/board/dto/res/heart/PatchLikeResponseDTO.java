package com.swyg.picketbackend.board.dto.res.heart;

import com.swyg.picketbackend.board.Entity.Heart;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;


import java.util.List;

@Data
@Builder
public class PatchLikeResponseDTO {


    @Schema(description = "로그인 회원 게시물 스크랩 여부", example = "1:누름 0: 안누름")
    private boolean isToggle;

    @Schema(description = "게시물 스크랩 총 개수", example = "좋아요 개수")
    private Long heartCount;

    public static PatchLikeResponseDTO of(boolean isToggle, List<Heart> heartList) {
        return PatchLikeResponseDTO.builder()
                .isToggle(isToggle)
                .heartCount((long) heartList.size())
                .build();
    }
}
