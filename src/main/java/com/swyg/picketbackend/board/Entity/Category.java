package com.swyg.picketbackend.board.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.log4j.Log4j2;


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

    public Category(Long categoryId) {
        this.id = categoryId;
    }
}
