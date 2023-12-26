package com.swyg.picketbackend.auth.service;

import com.swyg.picketbackend.auth.domain.Member;
import com.swyg.picketbackend.auth.domain.RefreshToken;
import com.swyg.picketbackend.auth.dto.auth.req.LoginDTO;
import com.swyg.picketbackend.auth.dto.auth.req.SignupRequestDTO;
import com.swyg.picketbackend.auth.dto.auth.req.TokenRequestDTO;
import com.swyg.picketbackend.auth.dto.auth.res.SignupResponseDTO;
import com.swyg.picketbackend.auth.dto.auth.res.TokenResponseDTO;
import com.swyg.picketbackend.auth.dto.member.PutPasswordDTO;
import com.swyg.picketbackend.auth.jwt.TokenProvider;
import com.swyg.picketbackend.auth.repository.MemberRepository;
import com.swyg.picketbackend.auth.repository.RefreshTokenRepository;
import com.swyg.picketbackend.auth.util.SecurityUtil;
import com.swyg.picketbackend.board.service.S3Service;
import com.swyg.picketbackend.global.exception.CustomException;
import com.swyg.picketbackend.global.util.ErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JavaMailSender javaMailSender;

    private final S3Service s3Service;


    // 회원 가입 이메일 중복 검사
    public void checkEmail(String email) {
        boolean result = memberRepository.existsByEmail(email);
        log.info(result);
        if (memberRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }
    }

    // 회원 가입 닉네임 중복 검사
    public void checkNickname(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        }
    }


    // 회원 가입 서비스
    @Transactional
    public void signup(SignupRequestDTO signupRequestDTO) throws CustomException {
        if (memberRepository.existsByEmail(signupRequestDTO.getEmail())) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }
        if (memberRepository.existsByNickname(signupRequestDTO.getNickname())) {
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        }

        Member member = signupRequestDTO.toMember(passwordEncoder);

        memberRepository.save(member);
    }


    // 로그인 서비스
    @Transactional
    public TokenResponseDTO login(LoginDTO loginDTO) {
        // 1. Login email/Password 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = loginDTO.toAuthentication();

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 PrincipalUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        log.info("authentication.getName() : " + authentication.getName());

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenResponseDTO tokenResponseDto = tokenProvider.generateTokenDto(authentication);

        Member member = memberRepository.findById(Long.valueOf(authentication.getName()))
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        tokenResponseDto.setNickname(member.getNickname());

        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenResponseDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        // 5. 토큰 발급
        return tokenResponseDto;
    }

    @Transactional
    public TokenResponseDTO reissue(TokenRequestDTO tokenRequestDto) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        TokenResponseDTO tokenResponseDto = tokenProvider.generateTokenDto(authentication);

        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenResponseDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 토큰 발급
        return tokenResponseDto;
    }

    @Transactional
    public SignupResponseDTO findMember(Long id) {

        Long currentMemberId = SecurityUtil.getCurrentMemberId(); // 현재 로그인한 유저 아이디 조회


        if (!currentMemberId.equals(id)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_BOARD_ACCESS); // 로그인한 사용자가 아니면 접근 권한 없음 throw exception
        }

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // entity -> dto
        return SignupResponseDTO.of(member);
    }


    // 비밀번호 변경 서비스
    public void passwordModify(PutPasswordDTO putPasswordDTO) {
        String email = putPasswordDTO.getEmail();

        // 메일에 해당하는 member get 존재하지 않으면 elseThrow
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.EMAIL_NOT_FOUND));

        String newPassword = generatePassword();

        log.info(newPassword);

        String encNewPassword = passwordEncoder.encode(newPassword);

        member.modifyPassword(encNewPassword);

        memberRepository.save(member); // dirty checking

        // 비밀번호 메일 전송 로직
        MimeMessage mimeMessage = createMessage(email, newPassword);
        log.info("Mail 전송 시작");
        javaMailSender.send(mimeMessage);
        log.info("Mail 전송 완료");
    }


    public String generatePassword() { // 비밀번호 생성 메서드

        StringBuilder password = new StringBuilder();

        while (password.length() < 8 || password.length() > 15) {
            char randomChar = getRandomChar();
            password.append(randomChar);
        }

        return password.toString();
    }


    public char getRandomChar() { // 랜덤 문자 생성 메서드

        SecureRandom random = new SecureRandom();
        int charType = random.nextInt(2);

        return switch (charType) {
            case 0 ->
                // 숫자를 무작위로 선택하여 '0'부터 '9'까지의 문자로 변환
                    (char) ('0' + random.nextInt(10));
            case 1 ->
                /// 소문자 알파벳을 무작위로 선택하여 'a'부터 'z'까지의 문자로 변환
                    (char) ('a' + random.nextInt(26));
            default ->
                // 이 부분은 예상치 못한 상황이 발생했을 때의 예외 처리
                    throw new IllegalStateException("Unexpected value: " + charType);
        };
    }

    public MimeMessage createMessage(String email, String newPassword) {  // 비밀번호 전송 메일 메서드
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom("picket@domain.com");  // TODO : 사이트 도메인으로 추후 수정할 것
            messageHelper.setTo(email);
            messageHelper.setSubject("[Picket] 비밀번호 변경 안내");

            String body = "<html><body style='background-color: #000000 !important; margin: 0 auto; max-width: 600px; word-break: break-all; padding-top: 50px; color: #ffffff;'>";
            body += "<h1 style='padding-top: 50px; font-size: 30px;'>비밀번호 변경 안내</h1>";
            body += "<p style='padding-top: 20px; font-size: 18px; opacity: 0.6; line-height: 30px; font-weight: 400;'>안녕하세요? Picket 관리자입니다.<br />";
            body += "계정의 새로운 비밀번호가 설정되었습니다.<br />";
            body += "하단의 새로운 비밀번호로 로그인 해주세요.<br />";
            body += "항상 최선의 노력을 다하는 Picket이 되겠습니다.<br />";
            body += "감사합니다.</p>";
            body += "<div class='code-box' style='margin-top: 50px; padding-top: 20px; color: #000000; padding-bottom: 20px; font-size: 25px; text-align: center; background-color: #f4f4f4; border-radius: 10px;'>" + newPassword + "</div>";
            body += "</body></html>";

            messageHelper.setText(body, true);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return mimeMessage;
    }


    public Boolean deleteTestS3(String fileName) {
        return s3Service.deleteFile(fileName);
    }


}
