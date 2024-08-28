package com.HotelBookingService.HotelBookingBackend.FeedbackModule.DTOs;

import com.HotelBookingService.HotelBookingBackend.FeedbackModule.FeedbackEntity;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
public class GetFeedbackDTO {

    private long roomId;
    private String feedback;
    private long userId;
    private String username;
    private long rating;
    private LocalDate feedbackDate;


    public GetFeedbackDTO makeGetFeedbackDTOFromFeedback(FeedbackEntity feedbackEntity, String username) {
        GetFeedbackDTO feedbackDTO = new GetFeedbackDTO();
        feedbackDTO.setFeedback(feedbackEntity.getFeedback());
        feedbackDTO.setRoomId(feedbackEntity.getFid().getRoomId());
        feedbackDTO.setUserId(feedbackEntity.getFid().getUserId());
        feedbackDTO.setRating(feedbackEntity.getRating());
        feedbackDTO.setFeedbackDate(feedbackEntity.getCreatedDate());

        feedbackDTO.setUsername(username);
        return feedbackDTO;
    }
}
