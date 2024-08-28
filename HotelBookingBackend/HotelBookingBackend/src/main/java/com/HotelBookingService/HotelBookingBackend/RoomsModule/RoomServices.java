package com.HotelBookingService.HotelBookingBackend.RoomsModule;

import com.HotelBookingService.HotelBookingBackend.RoomsModule.DTOs.AddRoomDTO;
import com.HotelBookingService.HotelBookingBackend.RoomsModule.DTOs.GetRoomDTO;
import com.HotelBookingService.HotelBookingBackend.RoomsModule.DTOs.UpdateRoomDTO;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomServices {
     boolean addRoom(MultipartFile image, AddRoomDTO roomEntity);
     boolean deleteRoom(Long roomId);
     boolean updateRoom(MultipartFile newImg, UpdateRoomDTO updateRoomDTO);
      List<GetRoomDTO> getAllRooms();
     Optional<GetRoomDTO> GetRoomById(Long roomId);
     List<GetRoomDTO> getAllRoomsAvailableBetweenDates(LocalDate startDate, LocalDate endDate);
     boolean isRoomAvailable(Long roomId, LocalDate startDate, LocalDate endDate);
     List<GetRoomDTO> getAllRoomsInZipCode(String zipCode);
     List<GetRoomDTO> getAllRoomsInZipCodeAndBetweenDates(String zipCode, LocalDate startDate, LocalDate endDate);
     List<GetRoomDTO> getAllRoomsByRoomType(String roomType);
}
