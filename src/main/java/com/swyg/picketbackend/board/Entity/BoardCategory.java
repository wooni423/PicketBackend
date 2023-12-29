package com.swyg.picketbackend.board.Entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.stream.Collectors;


@Log4j2
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "board_category")
public class BoardCategory { // Board와 category의 중간 테이블

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;


    public BoardCategory(Board board, Category category) {
        this.board = board;
        this.category = category;
    }


    // category -> boardCategory
    public static BoardCategory toEntity(Category category){
        return BoardCategory.builder()
                .category(category)
                .build();
    }

    // categoryList -> boardCategoryList
    public static List<BoardCategory> toEntityList(List<Category> categoryList){
        return categoryList.stream()
                .map(BoardCategory::toEntity)
                .toList();
    }

}
