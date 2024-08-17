package com.HotelBookingService.HotelBookingBackend.BookingModule;

import com.HotelBookingService.HotelBookingBackend.RoomsModule.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;



public interface BookingRepository extends JpaRepository<BookingEntity,Long> {

}
