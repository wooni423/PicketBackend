package com.swyg.picketbackend.board.repository;

import com.swyg.picketbackend.board.Entity.BoardCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardCategoryRepository  extends JpaRepository<BoardCategory, Long> {
}
