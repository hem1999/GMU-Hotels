package com.HotelBookingService.HotelBookingBackend.FeedbackModule;
import com.HotelBookingService.HotelBookingBackend.FeedbackModule.DTOs.AddFeedbackDTO;
import com.HotelBookingService.HotelBookingBackend.FeedbackModule.DTOs.GetFeedbackDTO;

import java.util.List;

public interface FeedbackService {

    List<FeedbackEntity> getAllFeedback();
    List<GetFeedbackDTO> getFeedbackByroomId(Long roomId);
    List<GetFeedbackDTO> getFeedbackByuserId(Long userId);
    boolean addFeedback(AddFeedbackDTO addFeedbackDTO);
    Integer averageRating(Long idValue, String idType);
}
