package com.HotelBookingService.HotelBookingBackend.BookingModule.DTOs;

import com.HotelBookingService.HotelBookingBackend.BookingModule.BookingEntity;
import com.HotelBookingService.HotelBookingBackend.RoomsModule.RoomEntity;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class GetBookingDTO {

    private Long bookingId;
    private LocalDateTime bookingDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private long roomId;
    private Long customerId;
    private String customerName;

    public GetBookingDTO makeGetBookingDTOFromBookingEntity(BookingEntity bookingEntity) {
        GetBookingDTO getBookingDTO = new GetBookingDTO();
        getBookingDTO.setBookingId(bookingEntity.getBookingId());
        getBookingDTO.setBookingDate(bookingEntity.getBookingDate());
        getBookingDTO.setStartDate(bookingEntity.getStartDate());
        getBookingDTO.setEndDate(bookingEntity.getEndDate());
        getBookingDTO.setCustomerId(bookingEntity.getUser().getUserId());
        getBookingDTO.setCustomerName(bookingEntity.getUser().getFirstName()+" "+bookingEntity.getUser().getLastName());
        getBookingDTO.setRoomId(bookingEntity.getRoom().getRoomId());
        return getBookingDTO;

    }

}
