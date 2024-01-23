package com.swyg.picketbackend.auth.service;

import com.swyg.picketbackend.auth.domain.Member;
import com.swyg.picketbackend.auth.util.Role;
import com.swyg.picketbackend.auth.util.SocialType;
import com.swyg.picketbackend.auth.repository.MemberRepository;
import com.swyg.picketbackend.auth.util.PrincipalDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;


@Component
@Log4j2
@RequiredArgsConstructor
public class PrincipalOauthDetailService extends DefaultOAuth2UserService { // 0Auth2.0 로그인을 성공하면 작동하는 class

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // TODO : https://picket.store/oauth2/authorization/google
    // 구글로부터 받은 userRequest 데이터에 대한 후처리되는 함수
    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("oAuthUser...");

        OAuth2User oAuth2User = super.loadUser(userRequest); // 소셜 로그인 정보 받아 옴

        // parameter setting
        String provider = userRequest.getClientRegistration().getRegistrationId();
        String nickname = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");
        String imageUrl = oAuth2User.getAttribute("picture");
        String providerId = provider + "_" + nickname;
        String password = "google";
        String encPassword = passwordEncoder.encode("google");

        // -> entity
        Member memberEntity = Member.builder()
                .email(email)
                .nickname(nickname)
                .imageUrl(imageUrl)
                .role(Role.ROLE_USER)
                .socialType(SocialType.GOOGLE)
                .password(encPassword)
                .providerId(providerId)
                .build();

        Member isMember =memberRepository.findByEmailAndProviderId(email,providerId);

        if (isMember==null) { // 처음 로그인이면 회원 가입 진행
            memberRepository.saveAndFlush(memberEntity);
        }



        return new PrincipalDetails(memberEntity, oAuth2User.getAttributes());
    }

}
