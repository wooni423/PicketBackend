package com.swyg.picketbackend.board.dto.res.board;

import com.swyg.picketbackend.board.Entity.Board;
import com.swyg.picketbackend.board.dto.util.BoardCategoryDTO;
import com.swyg.picketbackend.board.dto.util.CommentDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import com.swyg.picketbackend.board.Entity.Board;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SimpleBoardDTO {
    private Long id;
    private String title;
    private String content;
    private String nickname;
    private LocalDate deadline;
    private String filepath;
    private Long heartCount;
    private Long scrapCount;
    private List<BoardCategoryDTO> categoryList; // 카테고리 목록
    private List<CommentDTO> commentList; // 댓글 목록

    // Board 엔티티를 받아 SimpleBoardDTO를 초기화하는 생성자
    public SimpleBoardDTO(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.nickname = board.getMember().getNickname();
        this.deadline = board.getDeadline();
        this.filepath = board.getFilepath();
        this.heartCount = (long) board.getHeart().size();
        this.scrapCount = (long) board.getScrap().size();
        this.categoryList = BoardCategoryDTO.toCategoryDTOList(board.getBoardCategoryList()); // 카테고리 목록 설정
        this.commentList = CommentDTO.toCommentDTO(board.getCommentList()); // 댓글 목록 설정
    }

    // Lombok 어노테이션을 사용하여 게터, 세터, 기본 생성자가 자동으로 생성됩니다.
}

