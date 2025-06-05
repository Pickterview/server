package com.pickterview.member.dto.response;

import com.pickterview.common.enums.AuthProvider;
import com.pickterview.common.enums.Role;
import com.pickterview.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MemberSignupResponseDto {
    private Long id;
    private String email;
    private String name;
    private String tier;
    private String profileImage;
    private AuthProvider authProvider;
    private Integer exp;
    private Integer point;
    private Role role;
    private LocalDateTime createdAt;

    public static MemberSignupResponseDto fromEntity(Member member) {
        if (member == null) return null;
        return MemberSignupResponseDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .tier(member.getTier())
                .profileImage(member.getProfileImage())
                .authProvider(member.getAuthProvider())
                .exp(member.getExp())
                .point(member.getPoint())
                .role(member.getRole())
                .createdAt(member.getCreatedAt())
                .build();
    }
}