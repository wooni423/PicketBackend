package com.swyg.picketbackend.board.controller;

import com.swyg.picketbackend.auth.util.SecurityUtil;
import com.swyg.picketbackend.board.dto.res.heart.PatchLikeResponseDTO;
import com.swyg.picketbackend.board.service.HeartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "HeartController", description = "좋아요 관련 api")
@RequestMapping("/board")
@RequiredArgsConstructor
public class HeartController {

    private final HeartService heartService;

    @Operation(summary = "좋아요 클릭", description = "좋아요 클릭 1,해제 0 반환,총 좋아요 개수 반환")
    @PostMapping("/{boardId}/like") // 버킷 좋아요 클릭
    public ResponseEntity<?> clickLike(@PathVariable Long boardId) {
        Long currentLoginId = SecurityUtil.getCurrentMemberId();
        PatchLikeResponseDTO patchLikeResponseDTO = heartService.toggleHeart(boardId, currentLoginId);
        return ResponseEntity.ok().body(patchLikeResponseDTO);
    }

}