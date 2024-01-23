package com.swyg.picketbackend.board.dto.res.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDTO {


    private Long commentId; // 댓글 번호

    private Long boardId; // 댓글 게시글 번호

    private Long memberId; // 댓글 작성 회원 ID

    private String nickname; // 댓글 작성 회원 닉네임

    private String comment; // 댓글 내용
    

}
