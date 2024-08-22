package com.HotelBookingService.HotelBookingBackend.BookingModule;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;


public interface BookingRepository extends JpaRepository<BookingEntity,Long> {
    Integer roomAvailabilityBetweenStartDateAndEndDateButNotThisBooking(Long bookingId, Long roomId, LocalDate startDate, LocalDate endDate);
}
