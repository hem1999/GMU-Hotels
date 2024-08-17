package com.HotelBookingService.HotelBookingBackend.RoomsModule;

import com.HotelBookingService.HotelBookingBackend.RoomsModule.DTOs.AddRoomDTO;
import com.HotelBookingService.HotelBookingBackend.RoomsModule.DTOs.GetRoomDTO;
import com.HotelBookingService.HotelBookingBackend.RoomsModule.DTOs.UpdateRoomDTO;
import com.HotelBookingService.HotelBookingBackend.UserModule.UserEntity;
import com.HotelBookingService.HotelBookingBackend.UserModule.UserRepository;
import com.HotelBookingService.HotelBookingBackend.security.jwt.JwtService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@EnableMethodSecurity
@RestController
@RequestMapping("/rooms")
public class RoomsController {
    private final RoomServiceImpl roomServiceImpl;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public RoomsController(RoomServiceImpl roomServiceImpl, JwtService jwtService, UserRepository userRepository) {
        this.roomServiceImpl = roomServiceImpl;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }
    @GetMapping()
    public ResponseEntity<List<GetRoomDTO>> getAllRooms(){
        List<GetRoomDTO> rooms = this.roomServiceImpl.getAllRooms();
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<GetRoomDTO>> getAllRoomsByFilter(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate){
        List<GetRoomDTO> availableRooms = this.roomServiceImpl.getAllRoomsAvailableBetweenDates(startDate, endDate);
        return new ResponseEntity<>(availableRooms, HttpStatus.OK);
    }

    @GetMapping("/available")
    public ResponseEntity<?> isRoomAvailable(@RequestParam Long roomId, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate){
        try{
            boolean isAvailable = this.roomServiceImpl.isRoomAvailable(roomId, startDate, endDate);
            return new ResponseEntity<>(isAvailable, HttpStatus.OK);
        }catch (EntityNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<GetRoomDTO> getRoomById(@PathVariable("id") Long id){
        Optional<GetRoomDTO> r = this.roomServiceImpl.GetRoomById(id);
        return r.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<?> addRoom(@RequestParam("image") MultipartFile image,
                                              @RequestParam Map<String, Object> room, Authentication auth){
        //add current user into room map with creatorId key!

        UserDetails user = (UserDetails) auth.getPrincipal();
        UserEntity userEntity = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new EntityNotFoundException("User not found")
        );

        room.put("creatorId",userEntity.getUserId());
        AddRoomDTO addRoomDTO = new AddRoomDTO().makeAddRoomDTOFromMap(room);
        boolean res = this.roomServiceImpl.addRoom(image, addRoomDTO);
        if(res) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<UpdateRoomDTO> updateRoom(@RequestBody UpdateRoomDTO room){
        boolean res = this.roomServiceImpl.updateRoom(room);
        if(res) {
            return ResponseEntity.ok(room);
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteRoom(@PathVariable("id") Long id){
        boolean res = this.roomServiceImpl.deleteRoom(id);
        if(res) {
            return new ResponseEntity<>("Deleted",HttpStatus.OK);
        }
        return ResponseEntity.badRequest().build();
    }
}
