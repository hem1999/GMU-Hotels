package com.HotelBookingService.HotelBookingBackend.security.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationResponseDTO {
    private String jwtToken;
    private Long userId;
    private String userType;
}
