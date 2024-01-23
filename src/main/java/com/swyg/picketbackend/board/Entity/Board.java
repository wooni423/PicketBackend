package com.swyg.picketbackend.board.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.swyg.picketbackend.auth.domain.Member;
import com.swyg.picketbackend.board.dto.req.board.PatchBoardRequestDTO;
import com.swyg.picketbackend.board.dto.req.board.PostBoardRequestDTO;
import com.swyg.picketbackend.global.dto.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.hibernate.annotations.BatchSize;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// 지연 로딩으로 연관 테이블 객체들을 패치 조인하기 위한 Entity Graph 설정
@NamedEntityGraphs({
        @NamedEntityGraph(name = "Board.detail",
                attributeNodes = {
                        @NamedAttributeNode("member"),
                        @NamedAttributeNode("heart"),
                        @NamedAttributeNode("scrap"),
                        @NamedAttributeNode("boardCategoryList"),
                        @NamedAttributeNode(value = "boardCategoryList", subgraph = "boardCategoryListGraph"),
                        @NamedAttributeNode("commentList")
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "boardCategoryListGraph",
                                attributeNodes = {
                                        @NamedAttributeNode("category")
                                }
                        )
                }

        ),
        @NamedEntityGraph(name = "Board.search",
                attributeNodes = {
                        @NamedAttributeNode("member"),
                        @NamedAttributeNode("heart"),
                        @NamedAttributeNode("scrap"),
                        @NamedAttributeNode("boardCategoryList"),
                        @NamedAttributeNode(value = "boardCategoryList", subgraph = "boardCategoryListGraph")
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "boardCategoryListGraph",
                                attributeNodes = {
                                        @NamedAttributeNode("category")
                                }
                        )
                }

        )

}
)
@Log4j2
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Board extends BaseEntity {  // 생성날짜,수정날짜 자동 생성

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;  // 게시글 번호

    @Column(columnDefinition = "VARCHAR(3000)")
    private String title; // 게시글 제목

    @Column(columnDefinition = "VARCHAR(3000)")
    private String content; // 게시글 내용

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadline; // 종료 날짜

    @Column(columnDefinition = "VARCHAR(3000)")
    private String filename; // 파일 이름

    @Column(columnDefinition = "VARCHAR(3000)")
    private String filepath; // 파일 경로

    @Column
    private Long isCompleted; // 완료 여부  0: 진행 중  1: 완료

    @ManyToOne(fetch = FetchType.LAZY, optional = false) // member_id가 반드시 존재해야 함
    @JoinColumn(name = "member_id")
    @JsonManagedReference
    private Member member;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @BatchSize(size = 1000)
    @JsonManagedReference
    private List<Heart> heart = new ArrayList<>(); // 좋아요

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @BatchSize(size = 1000)
    @JsonManagedReference
    private List<Scrap> scrap = new ArrayList<>(); // 스크랩


    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @BatchSize(size = 1000)
    @JsonManagedReference
    private List<BoardCategory> boardCategoryList = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @BatchSize(size = 1000)
    private List<Comment> commentList = new ArrayList<>();


    // dto -> entity
    public static Board toEntity(PostBoardRequestDTO postBoardRequestDTO, Member member, String filename, String filepath) {
        return Board.builder()
                .title(postBoardRequestDTO.getTitle())
                .content(postBoardRequestDTO.getContent())
                .deadline(postBoardRequestDTO.getDeadline())
                .member(member)
                .filename(filename)
                .filepath(filepath)
                .heart(Collections.emptyList())
                .scrap(Collections.emptyList())
                .isCompleted(0L)
                .build();
    }

    // 버킷 업데이트 메서드
    public void updateBoard(PatchBoardRequestDTO patchBoardRequestDTO, String filename, String filepath) {
        this.title = patchBoardRequestDTO.getTitle();
        this.content = patchBoardRequestDTO.getContent();
        this.deadline = patchBoardRequestDTO.getDeadline();
        this.filename = filename;
        this.filepath = filepath;
    }


    // 댓글 등록을 위한 버킷 번호 set
    public static Board setId(Long boardId) {
        Board board = new Board();
        board.id = boardId;
        return board;
    }

    // 버킷 완료 처리 메서드
    public void finishedBoard() {
        this.isCompleted = 1L;
    }

}

