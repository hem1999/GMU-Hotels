package com.HotelBookingService.HotelBookingBackend.BookingModule;



import com.HotelBookingService.HotelBookingBackend.BookingModule.DTOs.AddBookingDTO;
import com.HotelBookingService.HotelBookingBackend.BookingModule.DTOs.GetBookingDTO;
import com.HotelBookingService.HotelBookingBackend.BookingModule.DTOs.UpdateBookingDTO;

import java.util.List;

public interface BookingServices {

     List<Long> addBooking(List<AddBookingDTO> addBookingDTO);
     boolean deleteBooking(Long booking_id);
     boolean updateBooking(UpdateBookingDTO updateBookingDTO);
     List<GetBookingDTO> getAllBookings();
     GetBookingDTO getBookingById(Long booking_id);
}
