package com.swyg.picketbackend.board.dto.util;



import com.swyg.picketbackend.board.Entity.BoardCategory;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class BoardCategoryDTO { // 버킷의 카테고리 ID 및 이름을 가져오는 DTO

    private Long categoryId;

    private String categoryName;


    // 버킷에서 조인해 온 버킷 카테고리에서 카테고리 이름 가져오는 메서드
    public static List<BoardCategoryDTO> toCategoryDTOList(List<BoardCategory> boardCategoryList) {
        List<BoardCategoryDTO> boardCategoryDTOList = new ArrayList<>();

        for(BoardCategory boardCategory : boardCategoryList){
            BoardCategoryDTO boardCategoryDTO = new BoardCategoryDTO();
            boardCategoryDTO.setCategoryId(boardCategory.getCategory().getId());
            boardCategoryDTO.setCategoryName(boardCategory.getCategory().getName());

            boardCategoryDTOList.add(boardCategoryDTO);
        }

        return boardCategoryDTOList;
    }
}
