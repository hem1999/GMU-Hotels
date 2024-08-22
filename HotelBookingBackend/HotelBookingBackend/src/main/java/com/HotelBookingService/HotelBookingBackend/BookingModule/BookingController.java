package com.HotelBookingService.HotelBookingBackend.BookingModule;


import com.HotelBookingService.HotelBookingBackend.BookingModule.DTOs.AddBookingDTO;
import com.HotelBookingService.HotelBookingBackend.BookingModule.DTOs.GetBookingDTO;
import com.HotelBookingService.HotelBookingBackend.BookingModule.DTOs.UpdateBookingDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final BookingServiceImpl bookingService;

    public BookingController(BookingServiceImpl bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public ResponseEntity<List<GetBookingDTO>> getBookings() {
        try {
            List<GetBookingDTO> b = this.bookingService.getAllBookings();
            return ResponseEntity.ok(b);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetBookingDTO> getBookingById(@PathVariable Long id) {
        GetBookingDTO b = this.bookingService.getBookingById(id);
        if (b != null) {
            return ResponseEntity.ok(b);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/addBooking")
    public ResponseEntity<?> addBooking(@RequestBody List<AddBookingDTO> addBookingDTOList) {

           try{
               return new ResponseEntity<>(this.bookingService.addBooking(addBookingDTOList), HttpStatus.CREATED);
           }catch (Exception e) {
               Map<String, Object> error = new HashMap<>();
               error.put("message", e.getMessage());
               return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
           }
    }

    @PutMapping("/updateBooking")
    public ResponseEntity<?> updateBooking(@RequestBody UpdateBookingDTO booking) {
        Map<String, Object> error = new HashMap<>();
        try {
            boolean isValid = this.bookingService.updateBooking(booking);
            if (isValid) {
                return new ResponseEntity<>(booking, HttpStatus.ACCEPTED);
            }
            error.put("message", "Booking update failed");
            return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);

        }catch (Exception e) {
            error.put("message", e.getMessage());
            return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable Long id) {
        boolean isValid = this.bookingService.deleteBooking(id);
        if (isValid) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
