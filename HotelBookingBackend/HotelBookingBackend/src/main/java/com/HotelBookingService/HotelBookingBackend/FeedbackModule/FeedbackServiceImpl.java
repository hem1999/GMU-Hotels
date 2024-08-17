package com.HotelBookingService.HotelBookingBackend.FeedbackModule;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    FeedbackRepository feedbackRepository;
    FeedbackServiceImpl(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }
    @Override
    public List<FeedbackEntity> getAllFeedback() {
        return this.feedbackRepository.findAll();
    }

    @Override
    public List<FeedbackEntity> getFeedbackByroomId(Long roomId) {
        return this.feedbackRepository.customGetFeedbackByRoomId(roomId);
    }

    @Override
    public List<FeedbackEntity> getFeedbackByuserId(Long userId) {
        return this.feedbackRepository.customGetFeedbackByUserId(userId);
    }

    @Override
    public boolean addFeedback(FeedbackEntity feedbackEntity) {
        try{
            this.feedbackRepository.save(feedbackEntity);
            return true;
        }catch(Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
}
