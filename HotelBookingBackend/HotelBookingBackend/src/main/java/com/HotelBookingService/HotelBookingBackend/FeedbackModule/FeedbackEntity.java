package com.HotelBookingService.HotelBookingBackend.FeedbackModule;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@NamedQueries({
        @NamedQuery(
                name = "FeedbackEntity.customGetFeedbackByRoomId",
                query = "select f from FeedbackEntity f where f.id.roomId=:roomId"
        ),
        @NamedQuery(
                name = "FeedbackEntity.customGetFeedbackByUserId",
                query = "select f from FeedbackEntity f where f.id.userId=:userId"
        )}
)
public class FeedbackEntity {

    @EmbeddedId
    private FeedbackId fid;

    private String feedback;

    private String rating;

    private LocalDate createdDate;
}
