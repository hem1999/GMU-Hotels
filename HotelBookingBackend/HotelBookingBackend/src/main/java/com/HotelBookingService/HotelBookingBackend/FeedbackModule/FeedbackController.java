package com.HotelBookingService.HotelBookingBackend.FeedbackModule;

import com.HotelBookingService.HotelBookingBackend.RoomsModule.RoomEntity;
import com.HotelBookingService.HotelBookingBackend.RoomsModule.RoomRepository;
import com.HotelBookingService.HotelBookingBackend.UserModule.UserEntity;
import com.HotelBookingService.HotelBookingBackend.UserModule.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/feedback")
public class FeedbackController {

    FeedbackServiceImpl feedbackService;
    RoomRepository roomRepository;
    UserRepository userRepository;
    FeedbackController(RoomRepository roomRepository, UserRepository userRepository, FeedbackServiceImpl feedbackService) {
        this.feedbackService = feedbackService;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    /* For a request like localhost:8081/feedback?idType=room&idValue=2
    * It will get feedbacks for room with id 2
    * Also it accepts localhost:8081/feedback?idType=user&idValue=2
    * It will get all the feedbacks given by user with id 2 */

    @GetMapping("")
    public ResponseEntity<List<FeedbackEntity>> feedbackForRoom(@RequestParam String idType, @RequestParam Long idValue ) {
        if(idType.equalsIgnoreCase("room")) {
            Optional<RoomEntity> r = this.roomRepository.findById(idValue);
            System.out.println("Is room present? "+r.isPresent());
            if (r.isPresent()) {
                System.out.println(this.feedbackService.getFeedbackByroomId(idValue).toString());
                return new ResponseEntity<>(this.feedbackService.getFeedbackByroomId(idValue), HttpStatus.OK);
            }else {
                return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
            }
        }
        else if(idType.equalsIgnoreCase("user")) {
            Optional<UserEntity> r = this.userRepository.findById(idValue);
            if (r.isPresent()) {
                return new ResponseEntity<>(this.feedbackService.getFeedbackByuserId(idValue), HttpStatus.OK);
            }else {
                return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
            }
        }
        else{
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }

    }



    @PostMapping("")
    public ResponseEntity<FeedbackEntity> addFeedbackController(@RequestBody FeedbackEntity feedbackEntity) {
        if(this.feedbackService.addFeedback(feedbackEntity)){
            return new ResponseEntity<>(feedbackEntity,HttpStatus.CREATED);
        }
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }
}
