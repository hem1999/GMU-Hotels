package com.HotelBookingService.HotelBookingBackend.UserModule;

import com.HotelBookingService.HotelBookingBackend.BookingModule.DTOs.GetBookingDTO;
import com.HotelBookingService.HotelBookingBackend.UserModule.DTOs.GetUserDTO;
import com.HotelBookingService.HotelBookingBackend.UserModule.DTOs.updateUserDTO;
import com.HotelBookingService.HotelBookingBackend.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/user")
@RequiredArgsConstructor
public class UserController {

    public final UserServiceImpl userServiceImpl;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @GetMapping(path = "/{id}")
    public ResponseEntity<GetUserDTO> getUser(@PathVariable Long id) {
        GetUserDTO ue = this.userServiceImpl.findUserById(id);
        if(ue == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ue);
    }

    //################ Removed Cause Register method serves this purpose #####################

    // TODO: do it later
//    @GetMapping(path = "/allUsers")
//    public ResponseEntity<List<UserEntity>> getAllUsers() {
//        List<UserEntity> lue = this.userServiceImpl.
//    }

//    @PostMapping(path = "/addUser")
//    public ResponseEntity<AddUserDTO> addUser(@RequestBody AddUserDTO user) {
//        if(this.userServiceImpl.addUser(user)){
//            return new ResponseEntity<>(user, HttpStatus.OK);
//        }
//        return ResponseEntity.badRequest().build();
//    }

    //################ Removed Cause Register method serves this purpose #####################

    @PutMapping(path="/updateUser")
    public ResponseEntity<updateUserDTO> updateUser(@RequestBody updateUserDTO updateUserDTO, @RequestHeader(name = "Authorization") String authHeader) {
        final String jwtToken = authHeader.substring(7);
        final String username = jwtService.extractUsername(jwtToken);
        UserEntity userFromJwt = userRepository.findByUsername(username).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );
        if(userFromJwt.getUserId() != updateUserDTO.getUserId()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if(this.userServiceImpl.updateUser(updateUserDTO)){
            return new ResponseEntity<>(updateUserDTO, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(updateUserDTO, HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(path="/deleteUser/{id}")
    public ResponseEntity<UserEntity> deleteUser(@PathVariable Long id) {
        if(this.userServiceImpl.deleteUser(id)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(path = "/userBookings/{id}")
    public ResponseEntity<Map<String, List<GetBookingDTO>>> getUserBookings(@PathVariable Long id) {
        Map<String, List<GetBookingDTO>> bookings = this.userServiceImpl.getBookings(id);
        return ResponseEntity.ok(bookings);
    }

}
