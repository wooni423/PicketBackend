package com.swyg.picketbackend.board.service;

import com.amazonaws.internal.SdkSSLContext;
import com.swyg.picketbackend.auth.domain.Member;
import com.swyg.picketbackend.auth.util.SecurityUtil;
import com.swyg.picketbackend.board.Entity.Board;
import com.swyg.picketbackend.board.Entity.Comment;
import com.swyg.picketbackend.board.dto.req.comment.PostCommentRequestDTO;
import com.swyg.picketbackend.board.repository.BoardRepository;
import com.swyg.picketbackend.board.repository.CommentRepository;
import com.swyg.picketbackend.global.exception.CustomException;
import com.swyg.picketbackend.global.util.ErrorCode;
import com.swyg.picketbackend.notification.controller.NotificationController;
import com.swyg.picketbackend.notification.service.NotificationService;
import com.swyg.picketbackend.notification.util.NotificationType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Log4j2
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final BoardRepository boardRepository;

    private final NotificationService notificationService;


    // 버킷 댓글 등록
    @Transactional
    public Comment addComment(Long boardId, PostCommentRequestDTO postCommentRequestDTO) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();

        if (currentMemberId.equals(0L)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_NEED_LOGIN);
        }

        Member member = Member.setId(currentMemberId); // 작성 회원 번호 set

        Board target = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND)); // 게시물 존재하는 지 확인

        Comment comment = Comment.toComment(member, target, postCommentRequestDTO.getContent()); // dto -> entity

        commentRepository.save(comment);

        // 댓글 알림
        String url = "/board/" + boardId;
        String content = target.getTitle() + "에 새로운 댓글이 달렸습니다.";

        notificationService.send(target.getMember(), NotificationType.COMMENT, content, url);


        return comment;

    }


    // 버킷 댓글 삭제
    @Transactional
    public void removeComment(Long boardId, Long commentId) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();

        Board findParent = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        Comment target = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        if (target.getMember().getId().equals(currentMemberId)) { // 로그인한 회원이 쓴 댓글인지 확인
            commentRepository.delete(target);
        } else {
            throw new CustomException(ErrorCode.UNAUTHORIZED_REPLY_DELETE);
        }
    }


}
