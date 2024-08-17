package com.HotelBookingService.HotelBookingBackend.FeedbackModule;
import java.util.List;

public interface FeedbackService {

    List<FeedbackEntity> getAllFeedback();
    List<FeedbackEntity> getFeedbackByroomId(Long roomId);
    List<FeedbackEntity> getFeedbackByuserId(Long userId);
    boolean addFeedback(FeedbackEntity feedback);
}
