package com.HotelBookingService.HotelBookingBackend.RoomsModule.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/*
* This is created to make sure and get only user_id and deal with it on server,
* Instead of taking whole user object in the request body.*/
@Getter
@Setter
public class AddRoomDTO {
    private String roomName;
    private String roomType;
    private String roomDescription;
    private int roomCapacity;
    private Long creatorId;
    private Integer pricePerNight;
    private String roomAddress;
    private String roomCity;
    private String roomState;
    private String roomZip;
    private String roomCountry;
    private String roomPhone;
    private String mainImgsrc;

    public AddRoomDTO makeAddRoomDTOFromMap(Map<String, Object> map) {
        AddRoomDTO addRoomDTO = new AddRoomDTO();
        addRoomDTO.setRoomName((String) map.get("roomName"));
        addRoomDTO.setRoomType((String) map.get("roomType"));
        addRoomDTO.setRoomDescription((String) map.get("roomDescription"));
        addRoomDTO.setRoomCapacity(Integer.parseInt(map.get("roomCapacity").toString()));
        addRoomDTO.setCreatorId((Long) map.get("creatorId"));
        addRoomDTO.setPricePerNight(Integer.parseInt(map.get("pricePerNight").toString()));
        addRoomDTO.setRoomAddress((String) map.get("roomAddress"));
        addRoomDTO.setRoomCity((String) map.get("roomCity"));
        addRoomDTO.setRoomState((String) map.get("roomState"));
        addRoomDTO.setRoomZip((String) map.get("roomZip"));
        addRoomDTO.setRoomCountry((String) map.get("roomCountry"));
        addRoomDTO.setRoomPhone((String) map.get("roomPhone"));
        addRoomDTO.setMainImgsrc((String) map.get("mainImgsrc"));
        return addRoomDTO;
    }

}
