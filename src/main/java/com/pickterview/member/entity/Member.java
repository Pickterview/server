package com.pickterview.member.entity;

import com.pickterview.common.enums.AuthProvider;
import com.pickterview.common.enums.Role;
import jakarta.persistence.*; // JPA 3.x (Spring Boot 3.x)

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
public class Member implements UserDetails {

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

// --- UserDetails 인터페이스 메소드 구현 ---

    /**
     * 사용자가 가진 권한 목록을 반환합니다.
     * 여기서는 Member 엔티티의 role 필드를 기반으로 권한을 설정합니다.
     * Spring Security는 일반적으로 "ROLE_" 접두사가 붙은 권한 이름을 기대합니다.
     * Role enum의 이름이 "USER", "ADMIN"이라면 "ROLE_USER", "ROLE_ADMIN" 형태로 만들어줍니다.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Role enum의 name() 메소드가 "ROLE_USER", "ROLE_ADMIN" 등을 반환한다고 가정합니다.
        // 만약 Role.USER.name()이 "USER"만 반환한다면 "ROLE_" + this.role.name() 처럼 접두사를 붙여야 할 수 있습니다.
        // 혹은 Role enum 자체에 getAuthority() 같은 메소드를 만들어 "ROLE_USER" 형태로 반환하게 할 수도 있습니다.
        if (this.role == null) {
            return Collections.emptyList();
        }
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
