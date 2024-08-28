package com.HotelBookingService.HotelBookingBackend.UserModule.DTOs;

import com.HotelBookingService.HotelBookingBackend.BookingModule.BookingEntity;
import com.HotelBookingService.HotelBookingBackend.BookingModule.DTOs.GetBookingDTO;
import com.HotelBookingService.HotelBookingBackend.RoomsModule.DTOs.GetRoomDTO;
import com.HotelBookingService.HotelBookingBackend.RoomsModule.RoomEntity;
import com.HotelBookingService.HotelBookingBackend.UserModule.UserEntity;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class GetUserDTO {
    private Long userId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Map<String, List<GetBookingDTO>> bookings;
    // It will have 3 keys: Previous (bookings before today),
    // Current (startDate < bookingDate < endDate)
    // Upcoming (startDate > today)
    private List<GetRoomDTO> rooms;
    private String userType;
    private String state;
    private String city;
    private String country;
    private String address;
    private String zip;

    public GetUserDTO makeGetUserDTOFromEntity(UserEntity userEntity) {
        GetUserDTO getUserDTO = new GetUserDTO();
        getUserDTO.setUserId(userEntity.getUserId());
        getUserDTO.setUsername(userEntity.getUsername());
        getUserDTO.setFirstName(userEntity.getFirstName());
        getUserDTO.setLastName(userEntity.getLastName());
        getUserDTO.setEmail(userEntity.getEmail());
        getUserDTO.setPhone(userEntity.getPhone());
        getUserDTO.setUserType(userEntity.getUserType());
        getUserDTO.setState(userEntity.getState());
        getUserDTO.setCity(userEntity.getCity());
        getUserDTO.setCountry(userEntity.getCountry());
        getUserDTO.setAddress(userEntity.getAddress());
        getUserDTO.setZip(userEntity.getZip());
        

        //Rooms:
        List<GetRoomDTO> rooms = new ArrayList<>();
        for (RoomEntity r : userEntity.getRooms()) {
//            rooms.add(new GetRoomDTO().makeGetRoomDTOFromRoomEntity(r));
            GetRoomDTO getRoomDTO = new GetRoomDTO().makeGetRoomDTOFromRoomEntity(r);
            rooms.add(getRoomDTO);
        }

        getUserDTO.setRooms(rooms);
        /* Getting the data same as GET bookings/:id will be more meaning ful!*/
        Map<String, List<GetBookingDTO>> bookings = new HashMap<>();
        List<GetBookingDTO> previous = new ArrayList<>();
        List<GetBookingDTO> current = new ArrayList<>();
        List<GetBookingDTO> upcoming = new ArrayList<>();
        for (BookingEntity b : userEntity.getBookings()) {
            System.out.println("Segregating started!");
            LocalDate start = b.getStartDate();
            LocalDate end = b.getEndDate();
            System.out.println(b.getStartDate());
            if(end.isBefore(LocalDate.now())) {
                previous.add(new GetBookingDTO().makeGetBookingDTOFromBookingEntity(b));
            }
            else if(start.isAfter(LocalDate.now())) {
                upcoming.add(new GetBookingDTO().makeGetBookingDTOFromBookingEntity(b));
            }else{
                current.add(new GetBookingDTO().makeGetBookingDTOFromBookingEntity(b));
            }
        }
        bookings.put("previous", previous);
        bookings.put("current", current);
        bookings.put("upcoming", upcoming);
        getUserDTO.setBookings(bookings);
        return getUserDTO;

    }

}
