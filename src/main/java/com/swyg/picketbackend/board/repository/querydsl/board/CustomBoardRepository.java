package com.swyg.picketbackend.board.repository.querydsl.board;

import com.swyg.picketbackend.board.Entity.Board;
import com.swyg.picketbackend.board.dto.req.board.GetBoardListRequestDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface CustomBoardRepository {

    Board findBoardWithDetails(Long boardId);

    Slice<Board> boardSearchList(Long lastBoardId,String keyword, List<Long> CategoryList, Pageable pageable);



}
