package com.swyg.picketbackend.board.repository;

import com.swyg.picketbackend.auth.domain.Member;
import com.swyg.picketbackend.board.Entity.Board;
import com.swyg.picketbackend.board.Entity.Heart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HeartRepository extends JpaRepository<Heart, Long> {

    List<Heart> findHeartsByBoard(Board board);

    Heart findByMemberAndBoard(Member member, Board board);
}

