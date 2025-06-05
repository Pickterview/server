package com.pickterview.member.controller;

import com.pickterview.member.dto.request.MemberLoginRequestDto;
import com.pickterview.member.dto.request.MemberSignupRequestDto;
import com.pickterview.member.dto.response.JwtToken;
import com.pickterview.member.dto.response.MemberResponseDto;
import com.pickterview.member.dto.response.MemberSignupResponseDto;
import com.pickterview.member.entity.Member;
import com.pickterview.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<?> signupMember(@Valid @RequestBody MemberSignupRequestDto signupRequestDto){
        // @Valid를 통해서 DTO의 유효성 검사가 자동으로 진행
        // 검증 실패 시 MethodArgumentNotValidException이 발생하고,
        // 이는 @ControllerAdvice를 통해 글로벌 예외 처리로 핸들링할 수 있음

        Member registeredMember = memberService.registerMember(signupRequestDto);
        MemberSignupResponseDto responseDto = MemberSignupResponseDto.fromEntity(registeredMember);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@Valid @RequestBody MemberLoginRequestDto loginRequestDto) { // @Valid 추가
        try {
            JwtToken jwtToken = memberService.signIn(loginRequestDto.getEmail(), loginRequestDto.getPassword());
            return ResponseEntity.ok(jwtToken); // OK는 기본적으로 200
        } catch (BadCredentialsException e) {
            // log.warn("Login failed for user {}: Invalid credentials", loginRequestDto.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이메일 또는 비밀번호가 일치하지 않습니다.");
        } catch (AuthenticationException e) {
            // log.warn("Login failed for user {}: {}", loginRequestDto.getEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponseDto> getCurrentMemberInfo(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            // 이 경우는 JwtAuthenticationFilter에서 걸러지므로 거의 발생하지 않지만, 방어적으로 처리
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Authentication 객체에서 사용자 이름(여기서는 이메일)을 가져옵니다.
        String userEmail;
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            userEmail = ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            userEmail = (String) principal;
        } else {
            // 예상치 못한 Principal 타입
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        Member member = memberService.findMemberByEmail(userEmail);
        MemberResponseDto responseDto = MemberResponseDto.fromEntity(member);
        return ResponseEntity.ok(responseDto);
    }

}
