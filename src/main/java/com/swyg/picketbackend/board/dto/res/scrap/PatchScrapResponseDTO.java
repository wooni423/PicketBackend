package com.swyg.picketbackend.board.dto.res.scrap;

import com.swyg.picketbackend.board.Entity.Scrap;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PatchScrapResponseDTO {

    @Schema(description = "좋아요 눌렀는지 여부", example = "1:누름 0: 안누름")
    private boolean isToggle;

    @Schema(description = "게시물 좋아요 총 개수", example = "좋아요 개수")
    private Long scrapCount;


    public static PatchScrapResponseDTO of(boolean isToggle, List<Scrap> scrapList) {
        return PatchScrapResponseDTO.builder()
                .isToggle(isToggle)
                .scrapCount((long) scrapList.size())
                .build();
    }
}
