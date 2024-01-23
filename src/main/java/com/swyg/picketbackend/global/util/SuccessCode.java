package com.swyg.picketbackend.global.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode {

    //회원 가입 관련
    EMAIL_CHECK_SUCCESS(HttpStatus.OK,"사용할 수 있는 이메일입니다."),

    NICKNAME_CHECK_SUCCESS(HttpStatus.OK,"사용할 수 있는 닉네임입니다."),

    SIGNUP_SUCCESS(HttpStatus.OK,"회원가입에 성공하였습니다."),

    SELECT_SUCCESS(HttpStatus.OK,"SELECT_SUCCESS"),

    UPDATE_SUCCESS(HttpStatus.OK,"UPDATE_SUCCESS"),

    // 비밀 번호 관련
    PASSWORD_UPDATE_MAIL_SUCCESS(HttpStatus.OK,"비밀번호 변경 메일을 전송하였습니다."),
    
    // 버킷 관련
    BOARD_UPDATE_SUCCESS(HttpStatus.OK,"버킷을 수정하였습니다."),
    BOARD_DELETE_SUCCESS(HttpStatus.OK,"버킷을 삭제하였습니다."),
    BOARD_INSERT_SUCCESS(HttpStatus.OK,"버킷이 추가되었습니다."),

    // 댓글 관련
    COMMENT_INSERT_SUCCESS(HttpStatus.OK,"댓글을 등록하였습니다."),
    COMMENT_DELETE_SUCCESS(HttpStatus.OK,"댓글을 삭제하였습니다."),


    DELETE_SUCCESS(HttpStatus.OK,"DELETE_SUCCESS"),

    INSERT_SUCCESS(HttpStatus.OK,"INSERT_SUCCESS"),

    BOARD_FINISHED_SUCCESS(HttpStatus.OK,"버킷리스트를 달성하였습니다.");

    private final HttpStatus status;
    private final String message;

}
