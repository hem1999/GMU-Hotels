package com.HotelBookingService.HotelBookingBackend.BookingModule.DTOs;

import com.HotelBookingService.HotelBookingBackend.BookingModule.BookingEntity;
import com.HotelBookingService.HotelBookingBackend.RoomsModule.RoomEntity;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDate;
import java.time.LocalDateTime;


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
    private String roomName;
    private String roomAddress, roomCity, roomState, roomZip, roomCountry, roomPhone, roomMainImgSrc;

    public GetBookingDTO makeGetBookingDTOFromBookingEntity(BookingEntity bookingEntity) {
        GetBookingDTO getBookingDTO = new GetBookingDTO();
        getBookingDTO.setBookingId(bookingEntity.getBookingId());
        getBookingDTO.setBookingDate(bookingEntity.getBookingDate());
        getBookingDTO.setStartDate(bookingEntity.getStartDate());
        getBookingDTO.setEndDate(bookingEntity.getEndDate());
        getBookingDTO.setCustomerId(bookingEntity.getUser().getUserId());
        getBookingDTO.setCustomerName(bookingEntity.getUser().getFirstName()+" "+bookingEntity.getUser().getLastName());
        RoomEntity roomEntity = bookingEntity.getRoom();
        getBookingDTO.setRoomId(roomEntity.getRoomId());
        getBookingDTO.setRoomName(roomEntity.getRoomName());
        getBookingDTO.setRoomAddress(roomEntity.getRoomAddress());
        getBookingDTO.setRoomCity(roomEntity.getRoomCity());
        getBookingDTO.setRoomState(roomEntity.getRoomState());
        getBookingDTO.setRoomZip(roomEntity.getRoomZip());
        getBookingDTO.setRoomCountry(roomEntity.getRoomCountry());
        getBookingDTO.setRoomPhone(roomEntity.getRoomPhone());
        getBookingDTO.setRoomMainImgSrc(roomEntity.getMainImgSrc());
        return getBookingDTO;
    }

}
