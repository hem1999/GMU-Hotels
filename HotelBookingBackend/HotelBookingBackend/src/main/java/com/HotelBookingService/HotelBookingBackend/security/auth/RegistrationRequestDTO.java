package com.HotelBookingService.HotelBookingBackend.security.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationRequestDTO {

    @NotEmpty // The annotated element must not be null nor empty.
    @NotBlank //The makes sure that username must not be null and must contain `at least one non-whitespace character`.
    private String username;
    @NotEmpty
    @NotBlank
    private String password;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String userType;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String country;
}
