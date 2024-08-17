package com.HotelBookingService.HotelBookingBackend.BookingModule;

import com.HotelBookingService.HotelBookingBackend.RoomsModule.RoomEntity;
import com.HotelBookingService.HotelBookingBackend.UserModule.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingEntity {
    @Id
    @GeneratedValue
    @Column()
    private Long bookingId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "fk_b_user_id")
    public UserEntity user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="fk_b_room_id")
    public RoomEntity room;

/*    For Learning purposes, I'm just letting the code be here!
    @JsonIgnore
    @JoinTable(
        name = "bookings_rooms",
        joinColumns={
                @JoinColumn(name = "booking_id")
        },
            inverseJoinColumns = {
                @JoinColumn(name = "room_id")
            }

    )
    @ManyToMany
    public List<RoomEntity> rooms;*/

    public LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime bookingDate;

}
