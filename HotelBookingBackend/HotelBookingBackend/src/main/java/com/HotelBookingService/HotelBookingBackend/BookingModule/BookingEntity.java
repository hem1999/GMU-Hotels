package com.HotelBookingService.HotelBookingBackend.BookingModule;

import com.HotelBookingService.HotelBookingBackend.RoomsModule.RoomEntity;
import com.HotelBookingService.HotelBookingBackend.UserModule.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@NamedQuery(
        name="BookingEntity.roomAvailabilityBetweenStartDateAndEndDateButNotThisBooking",
        query = "select 1 from RoomEntity  r where r.roomId=:roomId and not exists " +
                "(select 1 from BookingEntity  b where b.room=r and b.startDate<=:endDate and b.endDate>=:startDate and b.id!=:bookingId)"
)
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
