package com.swyg.picketbackend.auth.repository;

import com.swyg.picketbackend.auth.domain.Member;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member,Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Member findByEmailAndProviderId(String email, String providerId); // 이메일&소셜 타입으로 기존 로그인 소셜 회원인지 확인

}

