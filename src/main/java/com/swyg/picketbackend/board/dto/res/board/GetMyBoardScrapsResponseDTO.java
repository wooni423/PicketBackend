package com.swyg.picketbackend.board.dto.res.board;

import com.swyg.picketbackend.board.Entity.Board;
import com.swyg.picketbackend.board.Entity.Scrap;
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
public class GetMyBoardScrapsResponseDTO {  // 스크렙 한 게시물 정보


    @Schema(description = "버킷 번호", example = "1")
    private Long boardId;  // 게시글 번호

    @Schema(description = "버킷 제목", example = "버킷 제목")
    private String title; // 게시글 제목

    @Schema(description = "버킷 내용", example = "버킷 내용")
    private String content; // 게시글 내용

    @Schema(description = "버킷 이미지", example = "버킷 이미지 경로")
    private String bucketImg; // 파일 경로

    @Schema(description = "작성자 닉네임", example = "성자 닉네임")
    private String nickname; // 작성자 닉네임

    @Schema(description = "작성자 프로필 이미지", example = "작성자 프로필 이미지")
    private String profileImg; // 작성자 프로필 이미지

    @Schema(description = "마감기한", example = "2023-12-30")
    private LocalDate deadline; // 마감 기한

    @Schema(description = "버킷 완료 여부", example = "1 or 0")
    private Long isCompleted; // 마감 기한

    @Schema(description = "버킷 좋아요 수", example = "1")
    private Long heartCount; // 완료 버킷 수

    @Schema(description = "버킷 스크랩 수", example = "1")
    private Long scrapCount; // 진행 중 버킷 수


    // entity -> dto
    public static GetMyBoardScrapsResponseDTO of(Scrap scrap) {
        return GetMyBoardScrapsResponseDTO.builder()
                .boardId(scrap.getBoard().getId())
                .title(scrap.getBoard().getTitle())
                .content(scrap.getBoard().getContent())
                .bucketImg(scrap.getBoard().getFilepath())
                .nickname(scrap.getBoard().getMember().getNickname())
                .profileImg(scrap.getBoard().getMember().getImageUrl())
                .deadline(scrap.getBoard().getDeadline())
                .isCompleted(scrap.getBoard().getIsCompleted())
                .heartCount((long) scrap.getBoard().getHeart().size())
                .scrapCount((long) scrap.getBoard().getScrap().size())
                .build();
    }

    // entityList -> dtoList
    public static Slice<GetMyBoardScrapsResponseDTO> ofList(Slice<Scrap> boardList) {
        List<GetMyBoardScrapsResponseDTO> dtoList = boardList.stream()
                .map(GetMyBoardScrapsResponseDTO::of)
                .toList();

        return new SliceImpl<>(dtoList, boardList.getPageable(), boardList.hasNext());
    }
}
