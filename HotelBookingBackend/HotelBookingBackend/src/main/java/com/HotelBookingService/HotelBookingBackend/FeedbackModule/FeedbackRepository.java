package com.HotelBookingService.HotelBookingBackend.FeedbackModule;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<FeedbackEntity,FeedbackId> {


    List<FeedbackEntity> customGetFeedbackByRoomId(Long roomId);
    List<FeedbackEntity> customGetFeedbackByUserId(Long userId);
    Integer GetFeedbackRatingAvgByRoom(Long roomId);
    Integer GetFeedbackRatingAvgByUserId(Long userId);
}
