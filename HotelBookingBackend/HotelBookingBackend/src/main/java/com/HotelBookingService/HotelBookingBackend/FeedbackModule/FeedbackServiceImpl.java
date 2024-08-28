package com.HotelBookingService.HotelBookingBackend.FeedbackModule;

import com.HotelBookingService.HotelBookingBackend.FeedbackModule.DTOs.AddFeedbackDTO;
import com.HotelBookingService.HotelBookingBackend.FeedbackModule.DTOs.GetFeedbackDTO;
import com.HotelBookingService.HotelBookingBackend.UserModule.UserEntity;
import com.HotelBookingService.HotelBookingBackend.UserModule.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    FeedbackServiceImpl(FeedbackRepository feedbackRepository, UserRepository userRepository) {
        this.feedbackRepository = feedbackRepository;
        this.userRepository = userRepository;
    }
    @Override
    public List<FeedbackEntity> getAllFeedback() {
        return this.feedbackRepository.findAll();
    }

    @Override
    public List<GetFeedbackDTO> getFeedbackByroomId(Long roomId) {
        List<GetFeedbackDTO> feedbacks = new ArrayList<>();
        for(FeedbackEntity f : this.feedbackRepository.customGetFeedbackByRoomId(roomId)) {
            UserEntity ue = this.userRepository.findById(f.getFid().getUserId()).orElseThrow(
                    () -> new RuntimeException("User"+f.getFid().getUserId()+" not found")
            );

            feedbacks.add(new GetFeedbackDTO().makeGetFeedbackDTOFromFeedback(f,ue.getUsername()));
        }
        return feedbacks;
    }

    @Override
    public List<GetFeedbackDTO> getFeedbackByuserId(Long userId) {
        List<GetFeedbackDTO> feedbacks = new ArrayList<>();
        for(FeedbackEntity f : this.feedbackRepository.customGetFeedbackByUserId(userId)) {
            UserEntity ue = this.userRepository.findById(userId).orElseThrow(
                    () -> new RuntimeException("User"+userId+" not found")
            );
            feedbacks.add(new GetFeedbackDTO().makeGetFeedbackDTOFromFeedback(f,ue.getUsername()));
        }
        return feedbacks;
    }

    @Override
    public boolean addFeedback(AddFeedbackDTO addFeedbackDTO) {
        try{
            var feedback = FeedbackEntity.builder()
                    .fid(new FeedbackId(addFeedbackDTO.getUserId(), addFeedbackDTO.getRoomId()))
                    .feedback(addFeedbackDTO.getFeedback())
                    .rating(addFeedbackDTO.getRating())
                    .createdDate(LocalDate.now())
                    .build();
            this.feedbackRepository.save(feedback);
            return true;
        }catch(Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public Integer averageRating(Long idValue, String idType) {
        if(idType.equalsIgnoreCase("room")){
            return this.feedbackRepository.GetFeedbackRatingAvgByRoom(idValue);
        }
        else if(idType.equalsIgnoreCase("user")){
            return this.feedbackRepository.GetFeedbackRatingAvgByUserId(idValue);
        }
        throw new RuntimeException("Unsupported idType");
    }
}
