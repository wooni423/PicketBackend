package com.swyg.picketbackend.board.repository.querydsl.board;

import com.swyg.picketbackend.board.Entity.Board;
import com.swyg.picketbackend.board.Entity.Scrap;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface CustomBoardRepository {

    Board findBoardWithDetails(Long boardId);

    Slice<Board> findMyBoardList(Long memberId,Long lastBoardId,Pageable pageable);

    Slice<Board> findBoardSearchList(Long lastBoardId, String keyword, List<Long> CategoryList, Pageable pageable);

    Slice<Scrap> findMyScrapList(Long currentLoginId, Long lastBoardId, Pageable pageable);
}
