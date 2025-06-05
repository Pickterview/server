package com.pickterview.member.service;

import com.pickterview.member.entity.Member;
import com.pickterview.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // 필요한 경우

@Service // Spring 빈으로 등록
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    /**
     * Spring Security에서 사용자 인증 시 호출되는 메소드입니다.
     * @param email 사용자가 로그인 시 입력한 이메일 (UserDetails의 getUsername()과 매칭될 값)
     * @return UserDetails 인터페이스를 구현한 객체 (여기서는 Member 객체)
     * @throws UsernameNotFoundException 해당 이메일의 사용자를 찾을 수 없을 때 발생
     */
    @Override
    @Transactional(readOnly = true) // 데이터베이스 조회만 하므로 readOnly = true 설정 가능 (선택 사항)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다. 이메일: " + email));

        // Member 엔티티가 UserDetails를 구현했으므로 그대로 반환 가능
        // Member 엔티티의 getUsername()이 email을 반환하고,
        // getAuthorities()가 올바른 권한 정보를 반환하도록 구현되어 있어야 함
        return member;
    }
}
