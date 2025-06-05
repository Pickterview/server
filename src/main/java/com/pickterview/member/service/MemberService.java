package com.pickterview.member.service;

import com.pickterview.member.dto.request.MemberSignupRequestDto;
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
}