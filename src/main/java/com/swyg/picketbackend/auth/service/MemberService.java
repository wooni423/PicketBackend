package com.swyg.picketbackend.auth.service;


import com.swyg.picketbackend.auth.domain.Member;
import com.swyg.picketbackend.auth.dto.member.req.PatchMemberRequestDTO;
import com.swyg.picketbackend.auth.dto.member.res.PatchMemberResponseDTO;
import com.swyg.picketbackend.auth.repository.MemberRepository;
import com.swyg.picketbackend.auth.util.SecurityUtil;
import com.swyg.picketbackend.board.service.S3Service;
import com.swyg.picketbackend.global.exception.CustomException;
import com.swyg.picketbackend.global.util.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberService {

    private final MemberRepository memberRepository;

    private final S3Service s3Service;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public void nicknameCheck(String nickname) {
        boolean isExisted = memberRepository.existsByNickname(nickname);

        if (isExisted) { // 이미 존재하는 닉네임이면 예외 발생
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        }

    }

    @Transactional
    public PatchMemberResponseDTO modifyMemberWithFile(PatchMemberRequestDTO patchMemberRequestDTO, MultipartFile file) throws IOException {
        Long currentLoginId = SecurityUtil.getCurrentMemberId(); // 현재 로그인 한 ID

        // 1. 수정할 회원 찾기
        Member target = memberRepository.findById(currentLoginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        String targetFileName = target.getImageName(); // 회원의 기존 프로필 이미지 이름
        String targetFileUrl = target.getImageUrl(); // 회원의 기존 프로필 이미지 URl

        // 파일을 수정하지 않는 경우(기존 파일일 경우)
        if (targetFileName != null && targetFileUrl != null) { // 1. 기존 이미지가 존재하는 경우
            if (s3Service.areS3AndLocalFilesEqual(targetFileName, file)) {
                log.info("기존 파일일 경우 프로필 수정...");
                target.updateMember(patchMemberRequestDTO, targetFileName, targetFileUrl);
                try {
                    Member updateTarget = memberRepository.save(target); // dirty checking
                    return PatchMemberResponseDTO.of(updateTarget);
                } catch (IllegalArgumentException e) {
                    throw new CustomException(ErrorCode.UNAUTHORIZED_BOARD_DELETE);
                }

            }
        }

        log.info("새로운 파일일 경우 프로필 수정...");
        //  새로운 파일을 등록하는 경우
        UUID uuid = UUID.randomUUID();

        String newFilename = uuid + "_" + file.getOriginalFilename(); // 새로운 파일 이름

        String newFileUrl = "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + newFilename; // 새로운 파일 url

        // 1. 프로필 수정
        target.updateMember(patchMemberRequestDTO, newFilename, newFileUrl);
        Member updateTarget = memberRepository.save(target); // dirty checking

        // 2. Amazon S3 새로운 이미지 파일 저장
        s3Service.uploadFile(file, newFilename);

        // 3. Amazon S3 기존 이미지 삭제
        if (targetFileName != null && !targetFileName.isEmpty()) {
            s3Service.deleteFile(targetFileName);
        }

        return PatchMemberResponseDTO.of(updateTarget);
    }

}
