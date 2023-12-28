package com.swyg.picketbackend.board.dto.req.board;


import com.swyg.picketbackend.board.Entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetBoardSearchListRequestDTO {

    @Builder.Default
    private int page = 0;

    @Builder.Default
    private int size = 10;

    private String keyword;

    private List<Long> categoryList;


    // List<String> -> List<Category>로 변환
    public List<Category> toCategoryList() {
        List<Category> categoryList = new ArrayList<>();
        for (Long categoryId : this.categoryList) {
            categoryList.add(new Category(categoryId));
        }
        return categoryList;
    }
}
