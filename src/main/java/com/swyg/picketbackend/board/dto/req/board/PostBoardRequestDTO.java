package com.swyg.picketbackend.board.dto.req.board;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.swyg.picketbackend.board.Entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostBoardRequestDTO { // 게시글 작성 DTO

    @Schema(description = "버킷 제목", example = "버킷 제목")
    private String title; // 게시글 제목

    @Schema(description = "버킷 내용", example = "버킷 내용")
    private String content; // 게시글 내용

    @Schema(description = "버킷 종료일", example = "2023-12-30")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate deadline; // 종료 날짜

    @Schema(description = "버킷이 속한 카테고리 목록", example = "[\"category1\", \"category2\"]")
    private List<Long> categoryList; // 게시글이 속한 카테고리 목록


    // List<Long> -> List<Category>로 변환
    public List<Category> toCategoryList() {
        List<Category> categoryList = new ArrayList<>();
        for (Long categoryId : this.categoryList) {
            categoryList.add(new Category(categoryId));
        }
        return categoryList;
    }



}
