package com.HotelBookingService.HotelBookingBackend.RoomsModule;

import com.HotelBookingService.HotelBookingBackend.BookingModule.BookingEntity;
import com.HotelBookingService.HotelBookingBackend.UserModule.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@NamedQuery(
        name = "RoomEntity.availableRoomsBetweenStartDateAndEndDate",
        query= "select r from RoomEntity r where r not in (select b.room from BookingEntity b where b.startDate<=:endDate and b.endDate>=:startDate)"
)
@NamedQuery(
        name = "RoomEntity.roomAvailabilityBetweenStartDateAndEndDate",
        query= "select 1 from RoomEntity r where r.roomId = :roomId and not exists " +
                "(select 1 from BookingEntity b where b.room=r and b.startDate<=:endDate and b.endDate>=:startDate)"
)
public class RoomEntity {
    @Id
    @GeneratedValue
    @Column()
    private Long roomId;
    private String roomName;
    private String roomType;
    private int roomCapacity;
    @Column(columnDefinition = "TEXT")
    private String roomDescription;
    private int pricePerNight;
    private String roomAddress;
    private String roomCity;
    private String roomState;
    private String roomZip;
    private String roomCountry;
    private String roomPhone;
    private String mainImgSrc;

    //TODO: Room Address, Room Images
    //TODO: Location based filter like availability based filter.

    @OneToMany(mappedBy = "room")
    private List<BookingEntity> bookings;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "creator_id")
    private UserEntity createdByUser;
}
