package com.HotelBookingService.HotelBookingBackend.FeedbackModule;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class FeedbackId implements Serializable {

    private Long userId;
    private Long roomId;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeedbackId f = (FeedbackId) o;
        return Objects.equals(userId, f.userId) && Objects.equals(roomId, f.roomId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, roomId);
    }

}
