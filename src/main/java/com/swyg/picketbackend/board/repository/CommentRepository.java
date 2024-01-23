package com.swyg.picketbackend.board.repository;

import com.swyg.picketbackend.board.Entity.Comment;
import com.swyg.picketbackend.board.repository.querydsl.comment.CustomCommentRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>, CustomCommentRepository {

    @Query("SELECT c FROM Comment c JOIN FETCH c.member WHERE c.board.id = :boardId")
    List<Comment> findByBoardIdWithMember(Long boardId);

    //특정 닉네임의 모든 댓글 조회
    @Query(value = "SELECT * FROM comment WHERE nickname = :nickname", nativeQuery = true)
    List<Comment> findByNickname(String nickname);
}