package com.swyg.picketbackend.board.controller;

import com.swyg.picketbackend.board.dto.req.board.GetBoardListRequestDTO;
import com.swyg.picketbackend.board.dto.req.board.PostBoardRequestDTO;
import com.swyg.picketbackend.board.dto.res.board.GetBoardDetailsResponseDTO;
import com.swyg.picketbackend.board.dto.res.board.GetBoardListResponseDTO;
import com.swyg.picketbackend.board.dto.res.board.GetMyBoardListResponseDTO;
import com.swyg.picketbackend.board.dto.req.board.PatchBoardRequestDTO;
import com.swyg.picketbackend.board.service.BoardService;
import com.swyg.picketbackend.global.dto.SuccessResponse;
import com.swyg.picketbackend.global.exception.CustomException;
import com.swyg.picketbackend.global.util.ErrorCode;
import com.swyg.picketbackend.global.util.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Log4j2
@Tag(name = "BoardController", description = "버킷 관련 api")
@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;


    // Todo: 완료
    @Operation(summary = "나의 버킷리스트 조회", description = "로그인한 회원의 버킷리스트 조회 api 퀴리스트링에 memberId 필요")
    @GetMapping("/myposts") // 나의 버킷 리스트 조회
    public ResponseEntity<?> myBoardList() {
        List<GetMyBoardListResponseDTO> myBoardList = boardService.findMyBoardList();
        return (myBoardList == null || myBoardList.isEmpty()) ?
                ResponseEntity.status(HttpStatus.SC_OK).body("empty") :
                ResponseEntity.status(HttpStatus.SC_OK).body(myBoardList);
    }


    @Operation(summary = "전체&카테고리&검색 기반 버킷리스트 조회", description = "전체 또는 검색 조건에 따른 무한 스크롤 버킷리스트 목록을 반환하는 api")
    @GetMapping("/list/search") // 전체 버킷 리스트 조회
    public Slice<GetBoardListResponseDTO> boardSearchList(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "categoryList", required = false) List<Long> categoryList,
            @PageableDefault(size = 8, page = 0) Pageable pageable) {
        return boardService.searchBoardList(keyword, categoryList, pageable);

    }

    // TODO : 완료
    @Operation(summary = "버킷리스트 상세보기", description = "버킷리스트 상세보기 url에 boardId 필요")
    @GetMapping("/{boardId}")
    public GetBoardDetailsResponseDTO boardDetail(@PathVariable Long boardId) {
        return boardService.detailBoard(boardId);
    }

    // TODO : 완료
    @Operation(summary = "버킷리스트 작성", description = "카테고리는 번호 목록으로 넘겨줄 것 ex) categoryList:[1,2]")
    @PostMapping // 버킷리스트 작성
    public ResponseEntity<SuccessResponse> boardAdd(
            @RequestPart PostBoardRequestDTO postBoardRequestDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        try {
            if (file != null && !file.isEmpty()) {
                log.info("첨부파일이 있을 경우 insert...");
                boardService.addBoardWithFile(postBoardRequestDTO, file);
            } else {
                log.info("첨부파일 null 일 경우 insert...");
                boardService.addBoard(postBoardRequestDTO);
            }
        } catch (IOException e) {
            throw new CustomException(ErrorCode.IO_ERROR);
        }
        return SuccessResponse.success(SuccessCode.BOARD_INSERT_SUCCESS);
    }

    // TODO : 완료
    @Operation(summary = "버킷리스트 수정", description = "버킷리스트 수정 API")
    @PatchMapping("/{boardId}") // 버킷리스트 수정
    public ResponseEntity<SuccessResponse> boardModify(@PathVariable Long boardId,
                                                       @RequestPart PatchBoardRequestDTO patchBoardRequestDTO,
                                                       @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        try {
            if (file != null && !file.isEmpty()) {
                log.info("첨부파일이 있을 경우 modify...");
                boardService.modifyBoardWithFile(boardId, patchBoardRequestDTO, file);
            } else {
                log.info("첨부파일 null 일 경우 modify...");
                boardService.modifyBoard(boardId, patchBoardRequestDTO);
            }
        } catch (IOException e) {
            throw new CustomException(ErrorCode.IO_ERROR);
        }
        return SuccessResponse.success(SuccessCode.BOARD_UPDATE_SUCCESS);
    }


    // TODO: 완료
    @Operation(summary = "버킷리스트 삭제", description = "버킷리스트 삭제 boarId 쿼리스트링 필요")
    @DeleteMapping("/{boardId}") // 버킷리스트 삭제
    public ResponseEntity<SuccessResponse> boardRemove(@PathVariable Long boardId) {
        boardService.removeBoard(boardId);
        return SuccessResponse.success(SuccessCode.BOARD_DELETE_SUCCESS);
    }

}

