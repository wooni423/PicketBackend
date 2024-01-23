package com.swyg.picketbackend.board.dto.req.comment;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostCommentRequestDTO { // 댓글 작성 DTO

    private String content; // 댓글 내용
}
