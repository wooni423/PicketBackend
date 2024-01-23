package com.swyg.picketbackend.global.dto;


import com.swyg.picketbackend.global.util.SuccessCode;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
@RequiredArgsConstructor
public class SuccessResponse {

    private final HttpStatus status;
    private final String message;


    public static ResponseEntity<SuccessResponse> success(SuccessCode successCode) {
        return ResponseEntity
                .status(successCode.getStatus())
                .body(SuccessResponse.builder()
                        .status(successCode.getStatus())
                        .message(successCode.getMessage())
                        .build());
    }
}
