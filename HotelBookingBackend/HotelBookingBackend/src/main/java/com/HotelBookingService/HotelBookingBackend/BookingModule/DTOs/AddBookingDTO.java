package com.HotelBookingService.HotelBookingBackend.BookingModule.DTOs;

import lombok.Getter;
import lombok.Setter;


import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
public class AddBookingDTO {

    private Long userId;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime bookingDate;
    private Long roomId;
}
