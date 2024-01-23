package com.swyg.picketbackend.board.service;


import com.swyg.picketbackend.auth.domain.Member;
import com.swyg.picketbackend.board.Entity.Board;
import com.swyg.picketbackend.board.Entity.Heart;
import com.swyg.picketbackend.board.Entity.Scrap;
import com.swyg.picketbackend.board.dto.res.heart.PatchLikeResponseDTO;
import com.swyg.picketbackend.board.dto.res.scrap.PatchScrapResponseDTO;
import com.swyg.picketbackend.board.repository.BoardRepository;
import com.swyg.picketbackend.board.repository.ScrapRepository;
import com.swyg.picketbackend.global.exception.CustomException;
import com.swyg.picketbackend.global.util.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScrapService {

    private final ScrapRepository scrapRepository;

    private final BoardRepository boardRepository;


    @Transactional
    public PatchScrapResponseDTO toggleScrap(Long boardId, Long currentLoginId) {
        Board target = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        Optional<Scrap> existingScrapOpt = scrapRepository.findByBoardAndMemberId(target, currentLoginId);

        Member member = Member.setId(currentLoginId);

        Board board = Board.setId(boardId);

        if (existingScrapOpt.isPresent()) {
            scrapRepository.delete(existingScrapOpt.get());
            List<Scrap> scrapList = scrapRepository.findScrapsByBoard(board);
            return PatchScrapResponseDTO.of(false, scrapList);
        } else {
            Scrap newScrap = Scrap.addScrap(member, target);
            scrapRepository.save(newScrap);
            List<Scrap> scrapList = scrapRepository.findScrapsByBoard(board);
            return PatchScrapResponseDTO.of(true, scrapList);
        }
    }
}
