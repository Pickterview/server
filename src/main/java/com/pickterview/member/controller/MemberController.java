package com.pickterview.member.controller;

import com.pickterview.member.dto.request.MemberSignupRequestDto;
import com.pickterview.member.dto.response.MemberSignupResponseDto;
import com.pickterview.member.entity.Member;
import com.pickterview.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
