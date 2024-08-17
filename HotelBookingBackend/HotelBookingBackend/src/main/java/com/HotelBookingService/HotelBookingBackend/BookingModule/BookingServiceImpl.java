package com.HotelBookingService.HotelBookingBackend.BookingModule;

import com.HotelBookingService.HotelBookingBackend.BookingModule.DTOs.AddBookingDTO;
import com.HotelBookingService.HotelBookingBackend.BookingModule.DTOs.GetBookingDTO;
import com.HotelBookingService.HotelBookingBackend.BookingModule.DTOs.UpdateBookingDTO;
import com.HotelBookingService.HotelBookingBackend.RoomsModule.RoomEntity;
import com.HotelBookingService.HotelBookingBackend.RoomsModule.RoomRepository;
import com.HotelBookingService.HotelBookingBackend.UserModule.UserEntity;
import com.HotelBookingService.HotelBookingBackend.UserModule.UserRepository;
import com.HotelBookingService.HotelBookingBackend.security.email.EmailService;
import com.HotelBookingService.HotelBookingBackend.security.email.EmailTemplateName;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingServices{
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final EmailService emailService;
    public Long addOneBooking(AddBookingDTO addBookingDTO) {
        RoomEntity r = roomRepository.findById(addBookingDTO.getRoomId()).orElseThrow(
                () -> new EntityNotFoundException("Room not found with roomId: " + addBookingDTO.getRoomId())
        );
        UserEntity user = userRepository.findById(addBookingDTO.getUserId()).orElseThrow(
                () -> new UsernameNotFoundException("User not found with userId: " + addBookingDTO.getUserId())
        );
        if(roomRepository.roomAvailabilityBetweenStartDateAndEndDate(r.getRoomId(), addBookingDTO.getStartDate(), addBookingDTO.getEndDate())){
            BookingEntity b = BookingEntity.builder()
                    .bookingDate(addBookingDTO.getBookingDate())
                    .startDate(addBookingDTO.getStartDate())
                    .endDate(addBookingDTO.getEndDate())
                    .user(user)
                    .room(r)
                    .build();
            BookingEntity saved_b =  bookingRepository.save(b);
            return saved_b.getBookingId();
        }else{
            throw new RuntimeException("Room :"+r.getRoomName()+" is not available for asked dates");
        }

    }
    @Override
    public List<Long> addBooking(List<AddBookingDTO> addBookingDTO) {
        List<Long> bookingIds = new ArrayList<>();
        for(AddBookingDTO eachBooking : addBookingDTO){
            try{
                bookingIds.add(addOneBooking(eachBooking));
            }catch(Exception e){
                System.out.println(e.getMessage());
                return bookingIds; //These are the successful bookingIds!
            }
        }
        return bookingIds;
    }

    @Override
    public boolean deleteBooking(Long booking_id) {
        try {
            this.bookingRepository.deleteById(booking_id);
            return true;
        }catch(Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateBooking(UpdateBookingDTO updateBookingDTO) {
        Optional<BookingEntity> oldBooking = bookingRepository.findById(updateBookingDTO.getBookingId());
        if(oldBooking.isPresent()) {
            BookingEntity b = oldBooking.get();
            // TODO: First check for availabilities!
            if(roomRepository.roomAvailabilityBetweenStartDateAndEndDate(updateBookingDTO.getRoomId(), updateBookingDTO.getStartDate(), updateBookingDTO.getEndDate())){
                b.setStartDate(updateBookingDTO.getStartDate());
                b.setEndDate(updateBookingDTO.getEndDate());
                this.bookingRepository.save(b);
                return true;
            }else{
                throw new RuntimeException("For new dates, room is unavailable");
            }
           // TIP: Since you are sending UpdateBookingDTO as return value in response,
            // If any attribute is null, it will show as null in response,
            // So trust the updates with another GET request, not this
        }
        throw new EntityNotFoundException("Booking Not Found");

    }

    @Override
    public List<GetBookingDTO> getAllBookings() {
        List<GetBookingDTO> bookings = new ArrayList<>();
        for(BookingEntity b: this.bookingRepository.findAll()){
            bookings.add(new GetBookingDTO().makeGetBookingDTOFromBookingEntity(b));
        }
        return bookings;
    }

    @Override
    public GetBookingDTO getBookingById(Long booking_id) {
        Optional<BookingEntity> b = this.bookingRepository.findById(booking_id);
        if(b.isEmpty()){
            throw new EntityNotFoundException("Booking Not Found with id: " + booking_id);
        }
        return new GetBookingDTO().makeGetBookingDTOFromBookingEntity(b.get());
    }
}
