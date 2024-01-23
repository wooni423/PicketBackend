package com.swyg.picketbackend.board.controller;

import com.swyg.picketbackend.board.Entity.Comment;
import com.swyg.picketbackend.board.dto.req.comment.PostCommentRequestDTO;
import com.swyg.picketbackend.board.service.CommentService;
import com.swyg.picketbackend.global.dto.SuccessResponse;
import com.swyg.picketbackend.global.util.SuccessCode;
import com.swyg.picketbackend.notification.service.NotificationService;
import com.swyg.picketbackend.notification.util.NotificationType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Log4j2
@Tag(name = "CommentController", description = "버킷 댓글 관련 api")
@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    private final NotificationService notificationService;


    // 댓글 등록
    @Operation(summary = "댓글 등록 api", description = "버킷 번호(boardId)를 받아서 댓글 등록")
    @PostMapping("/{boardId}/comments")
    public ResponseEntity<SuccessResponse> commentAdd(@PathVariable Long boardId, @RequestBody PostCommentRequestDTO postCommentRequestDTO) {
        Comment result = commentService.addComment(boardId, postCommentRequestDTO);

        // 댓글 등록 알림
        Long boardMemberId = result.getBoard().getMember().getId(); // 버킷 작성 회원 ID
        log.info("boardMemberId : {}", boardMemberId);
        String title = result.getBoard().getTitle() + "에 새로운 댓글이 달렸습니다.";

        return SuccessResponse.success(SuccessCode.COMMENT_INSERT_SUCCESS);
    }

    // 댓글 삭제
    @Operation(summary = "댓글 삭제 api", description = "버킷 번호(boardId)&댓글 번호(commentId)를 통한 댓글 삭제 진행")
    @DeleteMapping("/{boardId}/comments/{commentId}")
    public ResponseEntity<SuccessResponse> commentRemove(
            @PathVariable Long boardId,
            @PathVariable Long commentId) {

        commentService.removeComment(boardId, commentId);
        return SuccessResponse.success(SuccessCode.COMMENT_DELETE_SUCCESS);
    }
}
