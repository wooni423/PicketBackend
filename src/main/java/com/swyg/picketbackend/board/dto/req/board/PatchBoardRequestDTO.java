package com.swyg.picketbackend.board.dto.req.board;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class PatchBoardRequestDTO {

    @Schema(description = "게시글 제목",example = "title")
    private String title; // 게시글 제목

    @Schema(description = "게시글 내용",example = "content")
    private String content; // 게시글 내용

    @Schema(description = "종료 날짜",example = "deadline")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd",timezone = "Asia/Seoul")
    private LocalDate deadline; // 종료 날짜
    
}
