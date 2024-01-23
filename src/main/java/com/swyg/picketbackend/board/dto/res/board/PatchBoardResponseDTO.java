package com.swyg.picketbackend.board.dto.res.board;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.swyg.picketbackend.board.Entity.Board;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatchBoardResponseDTO {


    @Schema(description = "수정된 버킷 제목", example = "title")
    private String title; // 게시글 제목

    @Schema(description = "수정된 버킷 내용", example = "content")
    private String content; // 게시글 내용

    @Schema(description = "수정된 버킷 이미지", example = "이미지 주소")
    private String bucketImg; // 게시글 내용

    @Schema(description = "종료 날짜", example = "deadline")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate deadline; // 종료 날짜


    public static PatchBoardResponseDTO of(Board board) {
        return PatchBoardResponseDTO.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .deadline(board.getDeadline())
                .bucketImg(board.getFilepath())
                .build();
    }
}
