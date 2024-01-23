package com.swyg.picketbackend.board.Entity;

import com.swyg.picketbackend.board.dto.res.board.GetMyBoardListResponseDTO;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.util.List;


@Log4j2
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;    // 카테고리 ID

    private String name; // 카테고리 이름

    public Category(Long categoryId) {
        this.id = categoryId;
    }


    // dto -> entity
    public static Category toEntity(Long categoryId) {
        return Category.builder()
                .id(categoryId)
                .build();
    }

    // dtoList -> entityList
    public static List<Category> toEntityList(List<Long> categoryList) {
        return categoryList.stream()
                .map(Category::toEntity)
                .toList();
    }
}
