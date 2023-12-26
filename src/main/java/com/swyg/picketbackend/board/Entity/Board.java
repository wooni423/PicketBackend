package com.swyg.picketbackend.board.Entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
import java.util.List;

@Log4j2
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "")
public class Board extends BaseEntity {  // 생성날짜,수정날짜 자동 생성

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;  // 게시글 번호

    @Column(columnDefinition = "TEXT")
    private String title; // 게시글 제목

    @Column(columnDefinition = "TEXT")
    private String content; // 게시글 내용

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadline; // 종료 날짜

    private String filename; // 파일 이름

    private String filepath; // 파일 경로

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
                .heart(null)
                .scrap(null)
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


}

