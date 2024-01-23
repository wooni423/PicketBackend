package com.swyg.picketbackend.board.dto.util;

import com.swyg.picketbackend.board.Entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class CommentDTO { // 버킷의 댓글 및 댓글을 단 회원의 닉네임을 가져오는 dto

    private Long id;

    private String nickname;

    private String content;

    private LocalDateTime createDate;

    private String profileUrl;


    public static List<CommentDTO> toCommentDTO(List<Comment> commentList) {
        List<CommentDTO> commentDTOList = new ArrayList<>();

        for (Comment comment : commentList) {
            CommentDTO commentDTO = new CommentDTO();
            commentDTO.setId(comment.getId());
            commentDTO.setNickname(comment.getMember().getNickname());
            commentDTO.setContent(comment.getContent());
            commentDTO.setCreateDate(comment.getCreateDate());
            commentDTO.setProfileUrl(comment.getMember().getImageUrl());

            commentDTOList.add(commentDTO);
        }

        return commentDTOList;
    }

}
