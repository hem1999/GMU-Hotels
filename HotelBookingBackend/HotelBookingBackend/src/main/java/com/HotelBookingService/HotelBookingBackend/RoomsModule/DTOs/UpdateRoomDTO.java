package com.HotelBookingService.HotelBookingBackend.RoomsModule.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateRoomDTO {
    private long roomId;
    private String roomName;
    private String roomDescription;
    private int roomCapacity;
    private String roomType;
    private int pricePerNight;
    private String roomAddress;
    private String roomCity;
    private String roomState;
    private String roomZip;
    private String roomCountry;
    private String roomPhone;

    //TODO: You need to do a lot if you have to update the image interms of managing s3! so not now!
}
