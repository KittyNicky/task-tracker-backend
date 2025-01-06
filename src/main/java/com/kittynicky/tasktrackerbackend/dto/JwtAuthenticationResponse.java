package com.kittynicky.tasktrackerbackend.dto;

import lombok.*;
import lombok.experimental.FieldNameConstants;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants(level = AccessLevel.PRIVATE)
public class JwtAuthenticationResponse {
    String accessToken;
    String refreshToken;
}
