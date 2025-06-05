package com.pickterview.member.service;

import com.pickterview.common.jwt.JwtTokenProvider;
import com.pickterview.member.dto.request.MemberSignupRequestDto;
import com.pickterview.member.dto.response.JwtToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pickterview.common.enums.AuthProvider;
import com.pickterview.common.enums.Role;
import com.pickterview.member.entity.Member;
import com.pickterview.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor // final 필드나 @NonNull 필드에 대한 생성자를 자동으로 생성해줍니다.
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager; // AuthenticationManagerBuilder 대신 주입


    @Transactional // 데이터 변경이 있으므로 트랜잭션 처리
    public Member registerMember(MemberSignupRequestDto signupRequestDto) {
        // 1. 이메일 중복 확인
        if (memberRepository.existsByEmail(signupRequestDto.getEmail())) {
            // 실제 프로젝트에서는 ErrorCode 등을 정의한 커스텀 예외를 사용하는 것이 좋습니다.
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다: " + signupRequestDto.getEmail());
            // 예시: throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        // 2. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());

        // 3. Member 엔티티 생성
        Member newMember = Member.builder()
                .email(signupRequestDto.getEmail())
                .password(encodedPassword) // 암호화된 비밀번호 사용
                .name(signupRequestDto.getName())
                .authProvider(AuthProvider.LOCAL) // 자체 회원가입이므로 LOCAL
                .role(Role.ROLE_USER) // 기본 역할 설정
                .build();

        // 4. 사용자 정보 저장
        return memberRepository.save(newMember);
    }

    @Transactional
    public JwtToken signIn(String username, String password) {
        // 1. username + password 를 기반으로 Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
        // 이때 CustomUserDetailsService (또는 Member 엔티티가 UserDetails를 구현한 방식)의 loadUserByUsername 메서드 실행
        Authentication authentication = authenticationManager.authenticate(authenticationToken); // 직접 사용

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        return jwtToken;
    }

    @Transactional(readOnly = true)
    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다. 이메일: " + email));
    }

}