package com.swyg.picketbackend.board;


import com.swyg.picketbackend.board.Entity.Board;
import com.swyg.picketbackend.board.repository.BoardRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
        Long boardId = 1L;

        // when


        // then

    }


}
