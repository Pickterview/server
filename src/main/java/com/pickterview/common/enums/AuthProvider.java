package com.pickterview.common.enums;

/**
 * 인증 공급자(auth provider)를 정의하는 enum
 * - LOCAL   : 자체 회원가입/로그인
 * - KAKAO   : 카카오 OAuth2 로그인
 * - GOOGLE  : 구글 OAuth2 로그인
 */
public enum AuthProvider {
    LOCAL,
    KAKAO,
    GOOGLE
}