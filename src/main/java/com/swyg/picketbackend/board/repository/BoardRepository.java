package com.swyg.picketbackend.board.repository;

import com.swyg.picketbackend.board.Entity.Board;
import com.swyg.picketbackend.board.repository.querydsl.board.CustomBoardRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>, CustomBoardRepository {

    List<Board> findAllByMemberId(Long memberId);

    @Query("select count(b) from Board b where b.isCompleted = :isCompleted and b.member.id= :memberId")
    Long countByIsCompletedAndMemberId(int isCompleted, Long memberId);  // 완료 혹은 미완료 버킷 개수 반환


}