package com.swyg.picketbackend.board.Entity;


import com.swyg.picketbackend.board.dto.util.BoardCategoryDTO;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;    // 카테고리 ID

    private String name; // 카테고리 이름


    // 카테고리 setting 생성자
    public Category(Long categoryId) {
        this.id = categoryId;
    }


    // 버킷에서 조인해 온 버킷 카테고리에서 카테고리 이름 가져오는 메서드
    public static List<Category> toCategoryList(List<BoardCategory> boardCategoryList) {
        List<Category> categoryList = new ArrayList<>();

        for (BoardCategory boardCategory : boardCategoryList) {
            Category category = boardCategory.getCategory();
            categoryList.add(category);
        }

        return categoryList;
    }

}
