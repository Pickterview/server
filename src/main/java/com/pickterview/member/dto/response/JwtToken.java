package com.pickterview.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class JwtToken {
    private String grantType;
    private String accessToken;
    private String refreshToken;
}
