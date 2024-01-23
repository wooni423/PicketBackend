package com.swyg.picketbackend.board.repository.querydsl.board;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.swyg.picketbackend.board.Entity.*;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class CustomBoardRepositoryImpl implements CustomBoardRepository {

    private final EntityManager entityManager;

    private final JPAQueryFactory jpaQueryFactory;

    QBoard board = QBoard.board;
    QBoardCategory boardCategory = QBoardCategory.boardCategory;
    QScrap scrap = QScrap.scrap;

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

    @Override
    public Slice<Scrap> findMyScrapList(Long currentLoginId, Long lastBoardId, Pageable pageable) {

        EntityGraph<?> entityGraph = entityManager.getEntityGraph("Scrap.myScrapBoardList");

        List<Scrap> resultList = jpaQueryFactory
                .selectFrom(scrap)
                .where(
                        isExistLastBoardId(lastBoardId),
                        scrap.member.id.eq(currentLoginId)
                )
                .orderBy(scrap.board.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return checkScrapsLastPage(pageable, resultList);
    }


    // 게시물 검색(전체,카테고리,검색어,no-offset 무한 스크롤 처리)
    @Override
    public Slice<Board> findBoardSearchList(Long lastBoardId, String keyword, List<Long> categoryList, Pageable pageable) {

        EntityGraph<?> entityGraph = entityManager.getEntityGraph("Board.search");

        List<Board> resultList = jpaQueryFactory
                .selectFrom(board)
                .distinct()
                .where(
                        keywordCondition(keyword),
                        categoryCondition(categoryList),
                        isExistLastBoardId(lastBoardId)
                )
                .orderBy(board.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();


        return checkLastPage(pageable, resultList);
    }


    // 키워드 검색 조건
    private BooleanExpression keywordCondition(String keyword) {
        if (keyword == null || keyword.isEmpty()) { // 키워드가 없으면 전체 검색
            return null;
        }
        return board.title
                .containsIgnoreCase(keyword)
                .or(board.content.containsIgnoreCase(keyword));
    }

    // 카테고리 검색 조건
    private BooleanExpression categoryCondition(List<Long> categoryList) {
        if (categoryList == null || categoryList.isEmpty()) { // 카테고리 따로 지정하지 않으면 전체 검색
            return null;
        }
        BooleanExpression[] categoryExpressions = categoryList.stream()
                .map(categoryId -> {
                    QBoardCategory qBoardCategory = board.boardCategoryList.any();
                    return qBoardCategory.category.id.eq(categoryId);
                })
                .toArray(BooleanExpression[]::new);

        return Expressions.anyOf(categoryExpressions);
    }


    // no -offset 방식 처리 메서드
    private BooleanExpression isExistLastBoardId(Long lastBoardId) {
        return lastBoardId == null ? null : board.id.lt(lastBoardId);

    }

    // Board 무한 스크롤 방식 처리 하는 메서드
    private Slice<Board> checkLastPage(Pageable pageable, List<Board> resultList) {
        boolean hasNext = false;

        // 조회한 결과 개수가 요청한 페이지 사이즈 보다 크면 뒤에 더 있음, next = true
        if (resultList.size() > pageable.getPageSize()) {
            hasNext = true;
            resultList.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(resultList, pageable, hasNext);
    }

    // Scrap 무한 스크롤 방식 처리 하는 메서드
    private Slice<Scrap> checkScrapsLastPage(Pageable pageable, List<Scrap> resultList) {
        boolean hasNext = false;

        // 조회한 결과 개수가 요청한 페이지 사이즈 보다 크면 뒤에 더 있음, next = true
        if (resultList.size() > pageable.getPageSize()) {
            hasNext = true;
            resultList.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(resultList, pageable, hasNext);
    }


}

