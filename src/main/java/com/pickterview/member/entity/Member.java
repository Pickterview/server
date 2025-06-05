package com.pickterview.member.entity;

import com.pickterview.common.enums.AuthProvider;
import com.pickterview.common.enums.Role;
import jakarta.persistence.*; // JPA 3.x (Spring Boot 3.x)

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * User 엔티티:
 * - id, email, password, name, tier, profileImage, authProvider, socialId, exp, point, createdAt, updatedAt
 * 에 더해, 이번에 'role' 컬럼을 추가합니다.
 */
@Entity
@Table(name = "member")   // 테이블명: member
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                    // 유저아이디 (PK, AUTO_INCREMENT)

    @Column(nullable = false, unique = true, length = 255)
    private String email;               // 이메일 (UNIQUE)

    @Column(nullable = true, length = 255)
    private String password;            // 비밀번호 (로컬 로그인용)

    @Column(nullable = false, length = 30)
    private String name;                // 이름

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String tier = "BRONZE3";    // 티어 (기본값: BRONZE3)

    @Column(name = "profile_image", length = 255)
    private String profileImage;        // 프로필 이미지 URL

    @Column(name = "auth_provider", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;  // 인증 제공자 (LOCAL, KAKAO, GOOGLE)

    @Column(name = "social_id", length = 255)
    private String socialId;            // 소셜ID (OAuth 로그인 시 사용)

    @Column(nullable = false)
    @Builder.Default
    private Integer exp = 0;            // 경험치 (기본값 0)

    @Column(nullable = false)
    @Builder.Default
    private Integer point = 0;          // 포인트 (기본값 0)

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;    // 생성일자 (자동 설정)

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;    // 수정일자 (자동 설정)

    /**
     * 추가된 부분: role 컬럼
     * - enum 타입(Role.java)
     * - 기본값은 ROLE_USER
     */
    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.ROLE_USER;
}
