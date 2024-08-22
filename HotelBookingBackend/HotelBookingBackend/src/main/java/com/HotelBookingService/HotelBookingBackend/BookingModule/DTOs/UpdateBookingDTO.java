package com.HotelBookingService.HotelBookingBackend.BookingModule.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateBookingDTO {

    private long bookingId;
    private Long roomId;
    private LocalDate startDate;
    private LocalDate endDate;

}
