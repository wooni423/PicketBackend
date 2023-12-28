package com.swyg.picketbackend.board.repository.querydsl.board;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.swyg.picketbackend.board.Entity.*;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class CustomBoardRepositoryImpl implements CustomBoardRepository {

    private final EntityManager entityManager;

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Board findBoardWithDetails(Long boardId) {

        QBoard boardDetails = QBoard.board;
        QCategory category = QCategory.category;
        EntityGraph<?> entityGraph = entityManager.getEntityGraph("Board.detail");


        return jpaQueryFactory
                .selectFrom(boardDetails)
                .where(boardDetails.id.eq(boardId),
                        boardDetails.boardCategoryList.isNotEmpty())
                .fetchOne();

        /*EntityGraph<?> entityGraph = entityManager.createEntityGraph(Board.class);
        entityGraph.addSubgraph("commentList");
        entityGraph.addSubgraph("boardCategoryList");
        entityGraph.addSubgraph("heart");
        entityGraph.addSubgraph("scrap");
        entityGraph.addSubgraph("member");

        return jpaQueryFactory
                .selectFrom(boardDetails)
                .where(boardDetails.id.eq(boardId))
                .fetchOne();*/
    }

    @Override
    public Slice<Board> boardSearchList(Long lastBoardId, String keyword, List<Long> categoryList, Pageable pageable) {
        QBoard board = QBoard.board;
        QBoardCategory boardCategory = QBoardCategory.boardCategory;

        EntityGraph<?> entityGraph = entityManager.createEntityGraph(Board.class);
        entityGraph.addSubgraph("commentList");
        entityGraph.addSubgraph("boardCategoryList");
        entityGraph.addSubgraph("heart");
        entityGraph.addSubgraph("scrap");
        entityGraph.addSubgraph("member");

        // 검색 조건
        BooleanExpression searchCondition = Expressions.asBoolean(true); // 초기 조건은 항상 참(카테고리&키워드가 없을때 전체 검색)

        // 카테고리가 비어있지 않은 경우 카테고리별 Board Id 조회
        if (categoryList != null && !categoryList.isEmpty()) {
            searchCondition = searchCondition.and(board.id.in(
                    JPAExpressions.select(boardCategory.board.id)
                            .from(boardCategory)
                            .where(boardCategory.category.id.in(categoryList))
                            .distinct()
            ));
        }

        // 검색어가 비어있지 않은 경우 검색어로 추가 필터링
        if (StringUtils.hasText(keyword)) {
            searchCondition = searchCondition.and(
                    board.title.containsIgnoreCase(keyword)
                            .or(board.content.containsIgnoreCase(keyword))
            );
        }

        // 페이징 및 무한 스크롤
        List<Board> boardList = jpaQueryFactory
                .selectFrom(board)
                .where(searchCondition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1) // 1개 더 가져와서 hasNext 여부 확인
                .fetch();


        return null;
    }


}

