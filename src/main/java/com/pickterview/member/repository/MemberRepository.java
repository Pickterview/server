package com.pickterview.member.repository;

import com.pickterview.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByEmail(String email); // 중복가입 여부 확인
    Optional<Member> findByName(String name);
    boolean existsByEmail(String email); // 이메일 존재 여부 확인
}
