package com.swyg.picketbackend.board;


import com.swyg.picketbackend.board.Entity.Board;
import com.swyg.picketbackend.board.dto.util.BoardCategoryDTO;
import com.swyg.picketbackend.board.repository.BoardRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import java.util.List;

@Log4j2
@SpringBootTest
@Transactional
public class BoardRepositoryTests {


    @Autowired
    private EntityManager entityManager;

    @Autowired
    private BoardRepository boardRepository;


    @Test
    public void findBoardDetailTests() {
        // Given
        Long boardId = 3L;

        // when
        Board board = boardRepository.findBoardWithDetails(boardId);

        // then
        List<BoardCategoryDTO> boardCategoryDTOList = board.getBoardCategoryList().stream()
                .map(boardCategory -> {
                    BoardCategoryDTO boardCategoryDTO = new BoardCategoryDTO();
                    boardCategoryDTO.setCategoryId(boardCategory.getCategory().getId());
                    boardCategoryDTO.setCategoryName(boardCategory.getCategory().getName());
                    return boardCategoryDTO;
                })
                .toList();

       /* for (BoardCategoryDTO boardCategoryDTO : boardCategoryDTOList) {
            log.info(boardCategoryDTO.getCategoryName());
        }*/
    }


    @Test
    @DisplayName("No-Offset 방식을 사용하면 lastStoreId값 -1 부터 page size 만큼 가져옴")
    public void findMyBoardListTests() {
        // given
        Slice<Board> boardList = boardRepository.findMyBoardList(1L, 10L, PageRequest.ofSize(6));

        // when
        Long first = boardList.getContent().get(0).getId();
        Long last = boardList.getContent().get(5).getId();

        // then
        Assertions.assertNotNull(boardList);
        Assertions.assertEquals(first, 9);
        Assertions.assertEquals(last, 4);
    }

    @Test
    @DisplayName("마지막 페이지에서는 isLast가 true, 마지막이 아니면 isLast가 false")
    public void checkLast() {
        // given
        Slice<Board> getLastPage = boardRepository.findMyBoardList(1L, 10L, PageRequest.ofSize(9));

        Slice<Board> getMiddlePage = boardRepository.findMyBoardList(1L,10L,PageRequest.ofSize(4));

        // when
        boolean isLastPage = getLastPage.isLast();
        boolean isNotLastPage = getMiddlePage.isLast();

        // then
        Assertions.assertTrue(isLastPage);
        Assertions.assertFalse(isNotLastPage);
    }


}



