package com.HotelBookingService.HotelBookingBackend.RoomsModule.DTOs;

import com.HotelBookingService.HotelBookingBackend.RoomsModule.RoomEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetRoomDTO {

    private long roomId;
    private String roomName;
    private String roomDescription;
    private String roomType;
    private int roomCapacity;
    private Long CreatorId;
    private String CreatorName;
    private Integer pricePerNight;
    private String roomAddress;
    private String roomCity;
    private String roomState;
    private String roomZip;
    private String roomCountry;
    private String roomPhone;
    private String mainImgSrc;

    public GetRoomDTO makeGetRoomDTOFromRoomEntity(RoomEntity r ) {
        GetRoomDTO getRoomDTO = new GetRoomDTO();
        getRoomDTO.setRoomId(r.getRoomId());
        getRoomDTO.setRoomName(r.getRoomName());
        getRoomDTO.setRoomDescription(r.getRoomDescription());
        getRoomDTO.setRoomType(r.getRoomType());
        getRoomDTO.setRoomCapacity(r.getRoomCapacity());
        getRoomDTO.setCreatorId(r.getCreatedByUser().getUserId());
        getRoomDTO.setCreatorName(r.getCreatedByUser().getFirstName() + " " + r.getCreatedByUser().getLastName());
        getRoomDTO.setPricePerNight(r.getPricePerNight());
        getRoomDTO.setRoomAddress(r.getRoomAddress());
        getRoomDTO.setRoomCity(r.getRoomCity());
        getRoomDTO.setRoomState(r.getRoomState());
        getRoomDTO.setRoomZip(r.getRoomZip());
        getRoomDTO.setRoomCountry(r.getRoomCountry());
        getRoomDTO.setRoomPhone(r.getRoomPhone());
        getRoomDTO.setMainImgSrc(r.getMainImgSrc());
        return getRoomDTO;
    }
}
