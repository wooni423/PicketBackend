package com.swyg.picketbackend.board.service;

import com.swyg.picketbackend.auth.domain.Member;
import com.swyg.picketbackend.board.Entity.Board;
import com.swyg.picketbackend.board.Entity.Heart;
import com.swyg.picketbackend.board.dto.res.heart.PatchLikeResponseDTO;
import com.swyg.picketbackend.board.repository.BoardRepository;
import com.swyg.picketbackend.board.repository.HeartRepository;
import com.swyg.picketbackend.global.exception.CustomException;
import com.swyg.picketbackend.global.util.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HeartService {

    private final HeartRepository heartRepository;

    private final BoardRepository boardRepository;

    @Transactional
    public PatchLikeResponseDTO toggleHeart(Long boardId, Long currentLoginId) {
        // 1. 좋아요 누를 버킷 확인
        Board target = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        Member member = Member.setId(currentLoginId);  // 회원번호 set

        Heart existingHeart = heartRepository.findByMemberAndBoard(member,target);

        Board board = Board.setId(boardId);  // 보드 번호 set



        if (existingHeart != null) {
            heartRepository.delete(existingHeart);
            List<Heart> heartList = heartRepository.findHeartsByBoard(board); // 총 좋아요 개수
            return PatchLikeResponseDTO.of(false,heartList);
        } else {
            Heart newHeart = Heart.addlike(member,target);
            heartRepository.save(newHeart);
            List<Heart> heartList = heartRepository.findHeartsByBoard(board); // 총 좋아요 개수
            return PatchLikeResponseDTO.of(true,heartList);
        }


    }

}
