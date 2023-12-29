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

import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class CustomBoardRepositoryImpl implements CustomBoardRepository {

    private final EntityManager entityManager;

    private final JPAQueryFactory jpaQueryFactory;

    QBoard board = QBoard.board;

    // 게시물 상세 보기
    @Override
    public Board findBoardWithDetails(Long boardId) {

        EntityGraph<?> entityGraph = entityManager.getEntityGraph("Board.detail");

        return jpaQueryFactory
                .selectFrom(board)
                .where(board.id.eq(boardId))
                .fetchOne();
    }


    // 나의 게시글 목록 보기(no-offset 무한 스크롤 처리)
    @Override
    public Slice<Board> findMyBoardList(Long memberId, Long lastBoardId, Pageable pageable) {

        List<Board> resultList = jpaQueryFactory
                .selectFrom(board)
                .where(
                        isExistLastBoardId(lastBoardId),
                        board.member.id.eq(memberId)
                )
                .orderBy(board.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return checkLastPage(pageable, resultList);
    }

    // no -offset 방식 처리 메서드
    private BooleanExpression isExistLastBoardId(Long lastBoardId) {
        return lastBoardId == null ? null : board.id.lt(lastBoardId);

    }

    // 무한 스크롤 방식 처리 하는 메서드
    private Slice<Board> checkLastPage(Pageable pageable, List<Board> resultList) {
        boolean hasNext = false;

        // 조회한 결과 개수가 요청한 페이지 사이즈 보다 크면 뒤에 더 있음, next = true
        if (resultList.size() > pageable.getPageSize()) {
            hasNext = true;
            resultList.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(resultList, pageable, hasNext);
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

