package com.swyg.picketbackend.board.controller;


import com.swyg.picketbackend.auth.util.SecurityUtil;
import com.swyg.picketbackend.board.dto.res.scrap.PatchScrapResponseDTO;
import com.swyg.picketbackend.board.service.ScrapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ScrapController", description = "스크랩 관련 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class ScrapController {

    private final ScrapService scrapService;
    
    // TODO : 완료
    @Operation(summary = "스크랩 클릭", description = "스크랩 클릭 1,해제 0 반환,총 스크랩 개수 반환")
    @PostMapping("/{boardId}/scrap")
    public ResponseEntity<?> toggleScrap(@PathVariable Long boardId) {
        Long currentLoginId = SecurityUtil.getCurrentMemberId();
        PatchScrapResponseDTO patchScrapResponseDTO = scrapService.toggleScrap(boardId, currentLoginId);
        return ResponseEntity.ok().body(patchScrapResponseDTO);
    }
}
