package com.HotelBookingService.HotelBookingBackend.FeedbackModule;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<FeedbackEntity,FeedbackId> {


    public List<FeedbackEntity> customGetFeedbackByRoomId(Long roomId);
    public List<FeedbackEntity> customGetFeedbackByUserId(Long userId);
}
