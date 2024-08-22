package com.HotelBookingService.HotelBookingBackend.security.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidateRequestDTO {
    private String jwtToken;
}
