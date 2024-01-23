package com.swyg.picketbackend.global.dto;

import com.swyg.picketbackend.global.exception.CustomException;
import com.swyg.picketbackend.global.util.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class ErrorResponse {

    private final HttpStatus status;
    private final String message;
    private final List<String> validErrors;


    public ErrorResponse(ErrorCode errorCode, List<String> messages) {
        this.status = errorCode.getStatus();
        this.message = errorCode.getMessage();
        this.validErrors = messages;
    }

    // RuntimeException 예외 처리 response 메서드
    public static ResponseEntity<ErrorResponse> error(CustomException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ErrorResponse.builder()
                        .status(e.getErrorCode().getStatus())
                        .message(e.getErrorCode().getMessage())
                        .build());
    }

    // MethodArgumentNotValidException 에외 처리 response 메서드(Valid)
    public static ResponseEntity<ErrorResponse> validError(MethodArgumentNotValidException e) {

        List<String> errorMessages = e.getBindingResult().getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .toList();


        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .validErrors(errorMessages)
                        .build());
    }


    // UserNameNotFound 예외 처리 response 메서드
    public static ResponseEntity<ErrorResponse> userNameNotFoundError(UsernameNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.builder()
                        .status(HttpStatus.UNAUTHORIZED)
                        .message(e.getMessage())
                        .build());
    }

    // UserNameNotFound 예외 처리 response 메서드
    public static ResponseEntity<ErrorResponse> numberFormatError(NumberFormatException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.builder()
                        .status(HttpStatus.UNAUTHORIZED)
                        .message("로그인이 필요합니다.")
                        .build());
    }
}
