package com.swyg.picketbackend.global.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /*400 BAD REQUEST : 클라이언트의 요청이 유효하지 않음*/
    PARAMETER_NOT_VALID(HttpStatus.BAD_REQUEST,"유효하지 않은 파라미터입니다."),

    // JWT 관련
    EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 토큰입니다."),
    UNAUTHENTICATION_ACCESS(HttpStatus.UNAUTHORIZED,"인증 회원 정보가 없습니다."),
    INVAILD_SIGNAUTRE(HttpStatus.BAD_REQUEST,"잘못된 JWT 서명입니다."),
    UNSUPPORTED_TOKEN(HttpStatus.BAD_REQUEST,"지원되지 않는 JWT 토큰입니다."),
    Illegal_TOKEN(HttpStatus.BAD_REQUEST,"잘못된 JWT 토큰입니다."),


    /*401 BAD Unauthorized : 권한 없음*/
    UNAUTHORIZED_BOARD_ACCESS(HttpStatus.UNAUTHORIZED,"접근 권한이 없습니다."),
    UNAUTHORIZED_BOARD_UPDATE(HttpStatus.UNAUTHORIZED,"버킷 수정 권한이 없습니다."),
    UNAUTHORIZED_BOARD_DELETE(HttpStatus.UNAUTHORIZED,"버킷 삭제 권한이 없습니다."),

    UNAUTHORIZED_REPLY_DELETE(HttpStatus.UNAUTHORIZED,"댓글 삭제 권한이 없습니다."),

    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED,"접근 권한이 없습니다."),

    UNAUTHORIZED_NEED_LOGIN(HttpStatus.UNAUTHORIZED,"로그인이 필요합니다."),

    /*403 Forbidden : 접근 금지*/
    FORBIDDEN(HttpStatus.FORBIDDEN,"접근 권한이 없습니다."),


    /*409 CONFLICT : 리소스가 현재 상태와 충돌,보통 중복된 데이터 존재*/
    DUPLICATE_EMAIL(HttpStatus.CONFLICT,"이미 존재하는 이메일입니다."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT,"이미 존재하는 닉네임입니다."),
    INSERT_FAILED(HttpStatus.CONFLICT,"등록에 실패하였습니다."),
    ALREADY_LIKED(HttpStatus.CONFLICT,"이미 좋아요를 누른 버킷입니다."),
    ALREADY_COMPLETED(HttpStatus.CONFLICT,"이미 달성 완료한 버킷입니다."),



    /*404 NOT_FOUND : 요청 리소스를 찾을 수 없음*/
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 회원을 찾을 수 없습니다."),
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 이메일의 회원을 찾을 수 업습니다."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "요청 리소스를 찾을 수 없습니다."),
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND,"버킷을 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND,"댓글을 찾을 수 없습니다."),

    /*500 NOT_FOUND : 서버 내부 오류*/
    DELETE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"내부 오류로 인해 버킷 삭제에 실패했습니다."),
    IO_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"파일 입출력 오류가 발생하였습니다."),
    NOTIFICATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"알림 메시지 발송 중 오류가 발생하였습니다."),
    
    // s3 관련
    S3_DELETE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"S3에서 이미지 삭제가 실패하였습니다.");


    private final HttpStatus status;
    private final String message;
}
