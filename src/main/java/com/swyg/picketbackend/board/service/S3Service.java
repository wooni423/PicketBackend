package com.swyg.picketbackend.board.service;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


@Log4j2
@RequiredArgsConstructor
@Service
public class S3Service {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    // 파일 업로드
    public void uploadFile(MultipartFile file, String fileName) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);
        log.info("s3 이미지 업로드 성공");
    }

    // 파일 삭제
    public Boolean deleteFile(String fileName) {
        try {
            // S3 버킷에서 파일 삭제
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
            System.out.println("S3에서 파일 삭제 완료");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("S3에서 파일 삭제 중 오류 발생");
            return false;
        }
    }


    // 기존 파일과 같은 파일인지 비교 메서드
    public boolean areS3AndLocalFilesEqual(String s3Key, MultipartFile file) {
        try (S3Object s3Object = amazonS3Client.getObject(new GetObjectRequest(bucket, s3Key));
             DigestInputStream multipartFileDis = new DigestInputStream(file.getInputStream(), MessageDigest.getInstance("MD5"))) {
            // S3 객체의 MD5 해시 계산
            String md5S3File = calculateMD5(s3Object.getObjectContent());
            log.info("md5S3File : {}", md5S3File);
            // Multipart file의 MD5 해시 계산
            String md5MultipartFile = calculateMD5(multipartFileDis);
            log.info("md5MultipartFile : {}", md5MultipartFile);
            return md5S3File.equals(md5MultipartFile);
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // 같은 파일인지 해쉬값으로 비교 메서드
    private static String calculateMD5(InputStream inputStream) throws IOException {
        try (DigestInputStream dis = new DigestInputStream(inputStream, MessageDigest.getInstance("MD5"))) {
            while (dis.read() != -1) {
                // S3 객체 또는 Multipart file의 내용을 읽어가면서 MD5 해시 계산
            }
            byte[] digest = dis.getMessageDigest().digest();
            return Base64.getEncoder().encodeToString(digest);
        } catch (Exception e) {
            throw new RuntimeException("MD5 calculation failed", e);
        }
    }
}
