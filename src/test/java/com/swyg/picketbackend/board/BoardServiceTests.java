package com.swyg.picketbackend.board;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.swyg.picketbackend.auth.domain.Member;
import com.swyg.picketbackend.board.Entity.*;
import com.swyg.picketbackend.board.repository.BoardCategoryRepository;
import com.swyg.picketbackend.board.repository.BoardRepository;
import com.swyg.picketbackend.board.repository.querydsl.board.CustomBoardRepositoryImpl;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@SpringBootTest
public class BoardServiceTests {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private BoardCategoryRepository boardCategoryRepository;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Autowired
    private CustomBoardRepositoryImpl customBoardRepositoryImpl;



    @Test
    @DisplayName("첨부파일 없는 버킷 등록 테스트")
    public void addBoardTests() {
        // given
        Member member = Member.setId(1L); // 회원번호 set

        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category(1L));
        categoryList.add(new Category(2L));

        // dto -> Entity
        Board board = Board.builder()
                .member(member)
                .title("버킷 등록 제목 테스트")
                .content("버킷 등록 내용")
                .deadline(LocalDate.of(2023, 12, 30))
                .build();

        Board saveBoard = boardRepository.save(board);

        for (Category category : categoryList) {
            BoardCategory boardCategory = new BoardCategory(saveBoard, category);
            boardCategoryRepository.save(boardCategory);
        }
        // then

    }


    @Test
    @DisplayName("버킷 상세조회 테스트")
    @Transactional
    public void detailBoardTests() {
        // given
        Long boardId = 1L;

        // when
        // then
    }
}
