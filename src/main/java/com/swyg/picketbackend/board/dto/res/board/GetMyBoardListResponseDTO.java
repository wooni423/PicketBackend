package com.swyg.picketbackend.board.dto.res.board;

import com.swyg.picketbackend.board.Entity.Board;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;


import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GetMyBoardListResponseDTO {

    @Schema(description = "버킷 번호", example = "1")
    private Long boardId;  // 게시글 번호

    @Schema(description = "버킷 제목", example = "버킷 제목")
    private String title; // 게시글 제목

    @Schema(description = "버킷 내용", example = "버킷 내용")
    private String content; // 게시글 내용

    @Schema(description = "마감기한", example = "2023-12-30")
    private LocalDate deadline; // 마감 기한

    @Schema(description = "파일 이름", example = "파일 이름")
    private String filename; // 파일 이름

    @Schema(description = "파일 경로", example = "이미지 실제 불러오는 경로")
    private String filepath; // 파일 경로

    @Schema(description = "버킷 완료 여부", example = "버킷 완료 여부")
    private Long isCompleted; // 버킷 완료

    @Schema(description = "완료 버킷 수", example = "완료 버킷 수")
    private Long finishTotal; // 완료 버킷 수

    @Schema(description = "진행 중 버킷 수", example = "완료 버킷 수")
    private Long progressTotal; // 진행 중 버킷 수


    // entity -> dto
    public static GetMyBoardListResponseDTO toDTO(Board board) {
        return GetMyBoardListResponseDTO.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .deadline(board.getDeadline())
                .filename(board.getFilename())
                .filepath(board.getFilepath())
                .isCompleted(board.getIsCompleted())
                .build();
    }

    // entityList -> dtoList
    public static Slice<GetMyBoardListResponseDTO> toDTOList(Slice<Board> boardList) {
        List<GetMyBoardListResponseDTO> dtoList = boardList.stream()
                .map(GetMyBoardListResponseDTO::toDTO)
                .toList();

        return new SliceImpl<>(dtoList, boardList.getPageable(), boardList.hasNext());
    }

}
