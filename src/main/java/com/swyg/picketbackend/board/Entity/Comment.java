package com.swyg.picketbackend.board.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.swyg.picketbackend.auth.domain.Member;
import com.swyg.picketbackend.global.dto.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    @JsonBackReference
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @JsonBackReference
    private Member member;

    private String content; // 댓글 내용


    // 댓글 등록 Entity 파라미터 setting 메서드
    public static Comment toComment(Member member, Board board,String content) {
        Comment comment = new Comment();
        comment.content = content; // 댓글 내용
        comment.member = member;
        comment.board = board;
        return comment;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", content='" + content + '\'' +
                '}';
    }
}
