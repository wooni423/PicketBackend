package com.swyg.picketbackend.heart;


import com.swyg.picketbackend.board.Entity.Board;
import com.swyg.picketbackend.board.Entity.Heart;
import com.swyg.picketbackend.board.repository.BoardRepository;
import com.swyg.picketbackend.board.repository.HeartRepository;
import com.swyg.picketbackend.global.exception.CustomException;
import com.swyg.picketbackend.global.util.ErrorCode;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Log4j2
@SpringBootTest
public class heartRepositoryTests {


    @Autowired
    private HeartRepository heartRepository;

    @Autowired
    private BoardRepository boardRepository;


    @Test
    @DisplayName("좋아요 입력 레포지토리 테스트")
    public void findHeartsByMemberTests() {
        // given
        Long boardId = 6L;
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
        // when
        List<Heart> heartList = heartRepository.findHeartsByBoard(board);

        // then
        assertThat(heartList).isNotNull();
        for (Heart heart : heartList) {
            log.info("heart member id {}", heart.getMember().getId());
        }

    }
}
