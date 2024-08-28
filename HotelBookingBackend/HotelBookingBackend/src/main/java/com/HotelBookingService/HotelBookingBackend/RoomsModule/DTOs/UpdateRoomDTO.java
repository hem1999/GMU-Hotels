package com.HotelBookingService.HotelBookingBackend.RoomsModule.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

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
    private String mainImgSrc;


    public UpdateRoomDTO makeUpdateRoomDTOFromMap(Map<String, Object> updateRoom) {
        UpdateRoomDTO updateRoomDTO = new UpdateRoomDTO();
        updateRoomDTO.setRoomId(Integer.parseInt((String)updateRoom.get("roomId")));
        updateRoomDTO.setRoomName((String) updateRoom.get("roomName"));
        updateRoomDTO.setRoomDescription((String) updateRoom.get("roomDescription"));
        updateRoomDTO.setRoomCapacity(Integer.parseInt((String)updateRoom.get("roomCapacity")));
        updateRoomDTO.setRoomType((String) updateRoom.get("roomType"));
        updateRoomDTO.setPricePerNight(Integer.parseInt((String) updateRoom.get("pricePerNight")));
        updateRoomDTO.setRoomAddress((String) updateRoom.get("roomAddress"));
        updateRoomDTO.setRoomCity((String) updateRoom.get("roomCity"));
        updateRoomDTO.setRoomState((String) updateRoom.get("roomState"));
        updateRoomDTO.setRoomZip((String) updateRoom.get("roomZip"));
        updateRoomDTO.setRoomCountry((String) updateRoom.get("roomCountry"));
        updateRoomDTO.setRoomPhone((String) updateRoom.get("roomPhone"));
        updateRoomDTO.setMainImgSrc((String) updateRoom.get("mainImgSrc"));
        return updateRoomDTO;
    }
}
