package com.swyg.picketbackend.auth.dto.member.req;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatchMemberRequestDTO {

    private String nickname; // 닉네임

}
