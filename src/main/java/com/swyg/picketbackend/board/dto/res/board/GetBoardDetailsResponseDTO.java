package com.swyg.picketbackend.board.dto.res.board;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swyg.picketbackend.board.Entity.Board;
import com.swyg.picketbackend.board.Entity.BoardCategory;
import com.swyg.picketbackend.board.dto.util.CommentDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;


@Getter
@ToString
@Builder
@AllArgsConstructor  // Generates a constructor for all fields
@NoArgsConstructor   // Generates a no-args constructor
public class GetBoardDetailsResponseDTO {

    @Schema(description = "게시글 번호", example = "1")
    private Long boardId;  // 게시글 번호

    @Schema(description = "버킷 제목", example = "버킷 제목")
    private String title; // 게시글 제목

    @Schema(description = "버킷 내용", example = "버킷 내용")
    private String content; // 게시글 내용

    @Schema(description = "버킷 작성자 닉네임", example = "닉네임")
    private String nickname; // 작성자 닉네임

    @Schema(description = "프로필 이미지", example = "프로필 이미지")
    private String profileImg;

    @Schema(description = "마감기한", example = "2023-12-30")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadline; // 마감 기한

    @Schema(description = "버킷 이미지 파일", example = "Amazon s3에 저장된 이미지 주소(https://swyg-picket.s3.ap-northeast-2.amazonaws.com" +
            "/87702a0e-299b-473a-b4a1-82663819c9d7_%EC%8A%A4%ED%94%84%EB%A7%81%EC%A7%84%EC%8A%A4.png)")
    private String filepath; // 파일 경로

    @Schema(description = "버킷 좋아요 개수", example = "5")
    private Long heartCount; // 좋아요 개수

    @Schema(description = "버킷 스크랩 개수", example = "10")
    private Long scrapCount; // 스크랩 개수

    @Schema(description = "버킷 카테고리", example = "[3 건강,4 자기개발]")
    private List<BoardCategory> categoryList;  // 카테고리 번호 및 이름가져오기

    @Schema(description = "버킷 댓글 목록", example = "댓글 작성자 닉네임,프로필 이미지, 댓글내용, 작성날짜, 수정날짜")
    private List<CommentDTO> commentList; // 게시글 댓글 리스트


    // entity -> dto
    public static GetBoardDetailsResponseDTO of(Board board) {
        return GetBoardDetailsResponseDTO.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .nickname(board.getMember().getNickname())
                .profileImg(board.getMember().getImageUrl())
                .deadline(board.getDeadline())
                .filepath(board.getFilepath())
                .heartCount((long) board.getHeart().size()) // 좋아요 수
                .scrapCount((long) board.getScrap().size()) // 댓글 수
                .categoryList(board.getBoardCategoryList())
                .commentList(CommentDTO.toCommentDTO(board.getCommentList()))
                .build();
    }

}
