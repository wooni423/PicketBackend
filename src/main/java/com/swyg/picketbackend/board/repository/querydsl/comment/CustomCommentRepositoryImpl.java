package com.swyg.picketbackend.board.repository.querydsl.comment;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CustomCommentRepositoryImpl implements CustomCommentRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    private final JPAQueryFactory jpaQueryFactory;

    /*@Override
    public List<CommentNicknameDTO> findCommentsNickname(Long boardId) {
        QComment comment = QComment.comment;

        return jpaQueryFactory
                .select(Projections.constructor(CommentNicknameDTO.class,
                        comment.id,
                        comment.member.nickname,
                        comment.content,
                        comment.createDate))
                .from(comment)
                .where(comment.board.id.eq(boardId))
                .fetch();
    }*/
}
