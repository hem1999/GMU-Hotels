package com.HotelBookingService.HotelBookingBackend.UserModule;


import com.HotelBookingService.HotelBookingBackend.BookingModule.DTOs.GetBookingDTO;
import com.HotelBookingService.HotelBookingBackend.UserModule.DTOs.GetUserDTO;
import com.HotelBookingService.HotelBookingBackend.UserModule.DTOs.updateUserDTO;

import java.util.List;
import java.util.Map;

public interface UserServices {
     GetUserDTO findUserByEmail(String email);
     GetUserDTO findUserByUsername(String username);
     GetUserDTO findUserById(Long id);
//     boolean addUser(AddUserDTO addUserDTO);
     boolean updateUser(updateUserDTO userEntity);
     boolean deleteUser(Long userId);
     Map<String, List<GetBookingDTO>> getBookings(Long userId);
}
