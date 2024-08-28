package com.HotelBookingService.HotelBookingBackend.FeedbackModule.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddFeedbackDTO {

    private long roomId;
    private String feedback;
    private long userId;
    private long rating;

}
