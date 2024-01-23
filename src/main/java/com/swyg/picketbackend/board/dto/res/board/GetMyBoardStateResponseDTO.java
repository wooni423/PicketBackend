package com.swyg.picketbackend.board.dto.res.board;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GetMyBoardStateResponseDTO {

    @Schema(description = "완료 버킷 수", example = "완료 버킷 수")
    private Long finishTotal;  //  완료 버킷 수

    @Schema(description = "진행 버킷 수", example = "진행 버킷 수")
    private Long progressTotal; // 진행 버킷 수


    public static GetMyBoardStateResponseDTO of(Long finishTotal,Long progressTotal) {
        return GetMyBoardStateResponseDTO.builder()
                .finishTotal(finishTotal)
                .progressTotal(progressTotal)
                .build();
    }
}
