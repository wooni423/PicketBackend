package com.swyg.picketbackend.auth.controller;

import com.swyg.picketbackend.auth.dto.auth.req.LoginDTO;
import com.swyg.picketbackend.auth.dto.auth.req.SignupRequestDTO;
import com.swyg.picketbackend.auth.dto.auth.res.TokenResponseDTO;
import com.swyg.picketbackend.auth.dto.auth.req.TokenRequestDTO;
import com.swyg.picketbackend.auth.dto.member.req.PutPasswordDTO;
import com.swyg.picketbackend.auth.service.AuthService;
import com.swyg.picketbackend.global.dto.SuccessResponse;
import com.swyg.picketbackend.global.exception.CustomException;
import com.swyg.picketbackend.global.util.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@Tag(name = "AuthController", description = "인증 관련 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    // TODO : 완료
    @Operation(summary = "회원가입 이메일 중복 검사", description = "회원 가입 이메일 중복 검사 API")
    @PostMapping("/signup/check-email")
    public ResponseEntity<SuccessResponse> emailCheck(@RequestBody String email) throws CustomException {
        log.info(email);
        authService.checkEmail(email);
        return SuccessResponse.success(SuccessCode.EMAIL_CHECK_SUCCESS);
    }

    // TODO : 완료
    @Operation(summary = "회원가입 닉네임 중복 검사", description = "회원 가입 닉네임 중복 검사 API")
    @PostMapping("/signup/check-nickname")
    public ResponseEntity<SuccessResponse> nicknameCheck(@RequestBody String nickname) throws CustomException {
        authService.checkNickname(nickname);
        return SuccessResponse.success(SuccessCode.NICKNAME_CHECK_SUCCESS);
    }

    // TODO : 완료
    @Operation(summary = "회원가입", description = "회원 가입 API")
    @PostMapping("/signup")
    public ResponseEntity<SuccessResponse> signup(@RequestBody SignupRequestDTO signupRequestDTO) throws CustomException {
        authService.signup(signupRequestDTO);
        return SuccessResponse.success(SuccessCode.SIGNUP_SUCCESS);
    }

    // TODO : 완료
    @Operation(summary = "로그인", description = "로그인을 통해 인증을 위한 엑세스 토큰 및 리프레쉬 토큰을 획득한다.(response body로 리턴)")
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok(authService.login(loginDTO));
    }

    // TODO : 완료
    @Operation(summary = "토큰 재발급", description = "토큰이 만료되거나 유효하지 않으면 재발급한다.(response body로 리턴)")
    @PostMapping("/reissue")
    public ResponseEntity<TokenResponseDTO> reissue(@RequestBody TokenRequestDTO tokenRequestDto) throws CustomException {
        return ResponseEntity.ok(authService.reissue(tokenRequestDto));
    }


    // TODO : 완료
    @Operation(summary = "가입 이메일로 비밀번호 초기화", description = "비밀번호 찾기 API")
    @PatchMapping("/reset-password")
    public ResponseEntity<SuccessResponse> modifyPassword(@RequestBody PutPasswordDTO putPasswordDTO) throws CustomException {
        log.info("/reset-password....");
        authService.passwordModify(putPasswordDTO);
        return SuccessResponse.success(SuccessCode.PASSWORD_UPDATE_MAIL_SUCCESS);
    }


    @Operation(summary = "소셜 로그인 test", description = "소셜 로그인 test")
    @GetMapping("/social-success")
    public String socialSuccess() {
        return "소셜 성공";
    }

}

