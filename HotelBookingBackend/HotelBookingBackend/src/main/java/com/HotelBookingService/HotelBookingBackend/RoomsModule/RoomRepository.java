package com.HotelBookingService.HotelBookingBackend.RoomsModule;



import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RoomRepository extends JpaRepository<RoomEntity,Long> {

    List<RoomEntity> availableRoomsBetweenStartDateAndEndDate(LocalDate startDate, LocalDate endDate);
    Integer roomAvailabilityBetweenStartDateAndEndDate(Long roomId, LocalDate startDate, LocalDate endDate);
    List<RoomEntity> roomsInLocation(String roomZip);
    List<RoomEntity> roomsInLocationAvailableBetweenStartDateAndEndDate(String roomZip,LocalDate startDate, LocalDate endDate);
    List<RoomEntity> findByRoomType(String roomType);
}
