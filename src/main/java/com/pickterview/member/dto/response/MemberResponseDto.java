package com.pickterview.member.dto.response;

import com.pickterview.common.enums.AuthProvider;
import com.pickterview.common.enums.Role;
import com.pickterview.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MemberResponseDto {
    private Long id;
    private String email;
    private String name;
    private String tier;
    private String profileImage;
    private AuthProvider authProvider; // 소셜 로그인 정보 등 필요시 포함
    private Integer exp;
    private Integer point;
    private Role role;
    private LocalDateTime createdAt;
    // 필요에 따라 추가 필드 (예: subTier)

    public static MemberResponseDto fromEntity(Member member) {
        if (member == null) return null;
        return MemberResponseDto.builder()
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