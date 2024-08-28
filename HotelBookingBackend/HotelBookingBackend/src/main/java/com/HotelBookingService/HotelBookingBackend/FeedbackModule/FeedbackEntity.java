package com.HotelBookingService.HotelBookingBackend.FeedbackModule;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@AllArgsConstructor
@Builder
@NamedQueries({
        @NamedQuery(
                name = "FeedbackEntity.customGetFeedbackByRoomId",
                query = "select f from FeedbackEntity f where f.id.roomId=:roomId"
        ),
        @NamedQuery(
                name = "FeedbackEntity.customGetFeedbackByUserId",
                query = "select f from FeedbackEntity f where f.id.userId=:userId"
        ),
        @NamedQuery(
                name="FeedbackEntity.GetFeedbackRatingAvgByRoom",
                query = "select avg(f.rating) from FeedbackEntity  f where f.id.roomId=:roomId"
        ),
        @NamedQuery(
                name="FeedbackEntity.GetFeedbackRatingAvgByUserId",
                query = "select avg(f.rating) from FeedbackEntity  f where f.id.userId=:userId"
        )


})
@NoArgsConstructor //When you use builder, noArgsConstructor is mandatory for the entity!
public class FeedbackEntity {

    @EmbeddedId
    private FeedbackId fid;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    private long rating;

    private LocalDate createdDate;
}
