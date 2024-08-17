package com.HotelBookingService.HotelBookingBackend.security.email;

import lombok.Getter;

@Getter
public enum EmailTemplateName {
    ACTIVATE_ACCOUNT("activate_account"),
    ACKNOWLEDGE_BOOKING("acknowledge_booking");

    private final String name;
    EmailTemplateName(final String name) {
        this.name = name;
    }
}
