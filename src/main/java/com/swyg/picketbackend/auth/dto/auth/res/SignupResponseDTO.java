package com.swyg.picketbackend.auth.dto.auth.res;


import com.swyg.picketbackend.auth.domain.Member;
import com.swyg.picketbackend.auth.util.Role;
import com.swyg.picketbackend.auth.util.SocialType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignupResponseDTO {

    private String email;


    // entity -> dto
    public static SignupResponseDTO of(Member member) {
        return SignupResponseDTO.builder()
                .email(member.getEmail())
                .build();

    }
}
