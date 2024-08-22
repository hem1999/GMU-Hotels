package com.HotelBookingService.HotelBookingBackend.security.email;

import lombok.Getter;

@Getter
public enum EmailTemplateName {
    ACTIVATE_ACCOUNT("activate_account"),
    BOOKING_CONFIRMATION("booking_confirmation");

    private final String name;
    EmailTemplateName(final String name) {
        this.name = name;
    }
}
