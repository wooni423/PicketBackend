package com.swyg.picketbackend.comment;


import com.swyg.picketbackend.auth.domain.Member;
import com.swyg.picketbackend.board.Entity.Board;
import com.swyg.picketbackend.board.Entity.Comment;
import com.swyg.picketbackend.board.repository.BoardRepository;
import com.swyg.picketbackend.board.repository.CommentRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;



@Log4j2
@SpringBootTest
public class CommentServiceTests {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Test
    @DisplayName("댓글 등록 테스트")
    public void addBoardTests() {
        // given
        Member member = Member.setId(1L); // 작성 회원 번호 set
        Board board = Board.setId(5L); // 버킷 번호 set
        String content = "댓글 등록 테스트";

        Comment comment = Comment.toComment(member, board, content);


        commentRepository.save(comment);

        // then

    }
    
/*    @Test
    @DisplayName("댓글 삭제 테스트")
    public void removeComment() {
        // given
        Member member = Member.setId(2L);
        Board board = Board.setId(5L);

        // when
        Board findParent = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        Comment target = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        // then
    }*/


}
