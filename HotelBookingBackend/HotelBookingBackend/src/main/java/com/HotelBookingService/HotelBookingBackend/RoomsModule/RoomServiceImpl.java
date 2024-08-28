package com.HotelBookingService.HotelBookingBackend.RoomsModule;



import com.HotelBookingService.HotelBookingBackend.RoomsModule.DTOs.AddRoomDTO;
import com.HotelBookingService.HotelBookingBackend.RoomsModule.DTOs.GetRoomDTO;
import com.HotelBookingService.HotelBookingBackend.RoomsModule.DTOs.UpdateRoomDTO;
import com.HotelBookingService.HotelBookingBackend.RoomsModule.cloud.S3Service;
import com.HotelBookingService.HotelBookingBackend.UserModule.UserEntity;
import com.HotelBookingService.HotelBookingBackend.UserModule.UserRepository;
import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomServices{


    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;



    @Override
    public boolean addRoom(MultipartFile image, AddRoomDTO roomEntity) {
        try {
            if(roomEntity.getCreatorId() == null){
                throw new IllegalArgumentException("Creator id cannot be null");
            }

            UserEntity creator = this.userRepository.findById(roomEntity.getCreatorId()).orElseThrow(
                    () -> new EntityNotFoundException("User not found"));
            var r = RoomEntity.builder()
                    .roomName(roomEntity.getRoomName())
                    .roomDescription(roomEntity.getRoomDescription())
                    .roomAddress(roomEntity.getRoomAddress())
                    .roomCountry(roomEntity.getRoomCountry())
                    .roomPhone(roomEntity.getRoomPhone())
                    .roomType(roomEntity.getRoomType())
                    .roomState(roomEntity.getRoomState())
                    .roomCapacity(roomEntity.getRoomCapacity())
                    .roomCity(roomEntity.getRoomCity())
                    .roomZip(roomEntity.getRoomZip())
                    .pricePerNight(roomEntity.getPricePerNight())
                    .createdByUser(creator)
                    .build();

            var updatedRoom = this.roomRepository.save(r);
            String mainImgSrc = updatedRoom.getCreatedByUser().getUserId()+"/"+updatedRoom.getRoomId()+"/mainImg.jpg";
            String mainImgSrcUrl = s3Service.uploadFile(image, mainImgSrc);

            updatedRoom.setMainImgSrc(mainImgSrcUrl);
            this.roomRepository.save(updatedRoom);
            return true;
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteRoom(Long roomId) {
        RoomEntity roomEntity = this.roomRepository.findById(roomId).orElseThrow(
                () -> new EntityNotFoundException("Room not found with id: " + roomId)
        );
        String mainImgSrcKey = roomEntity.getCreatedByUser().getUserId()+"/"+roomEntity.getRoomId()+"/mainImg.jpg";
        //TODO: How key differs from URL?
        s3Service.deleteFile(mainImgSrcKey);//Here we are passing url, but all it needs is key!
        roomRepository.delete(roomEntity);
        return true;
    }

    @Override
    public boolean updateRoom(MultipartFile newImg, UpdateRoomDTO updateRoomDTO) {
        Optional<RoomEntity> r = this.roomRepository.findById(updateRoomDTO.getRoomId());
        if(r.isEmpty()){
            throw new EntityNotFoundException("Room not found");
        }
        RoomEntity roomEntity = r.get();
        if(updateRoomDTO.getRoomName() != null){
            roomEntity.setRoomName(updateRoomDTO.getRoomName());
        }
        if(updateRoomDTO.getRoomDescription() != null){
            roomEntity.setRoomDescription(updateRoomDTO.getRoomDescription());
        }
        if(updateRoomDTO.getRoomAddress() != null){
            roomEntity.setRoomAddress(updateRoomDTO.getRoomAddress());
        }
        if(updateRoomDTO.getRoomCountry() != null){
            roomEntity.setRoomCountry(updateRoomDTO.getRoomCountry());
        }
        if(updateRoomDTO.getRoomPhone() != null){
            roomEntity.setRoomPhone(updateRoomDTO.getRoomPhone());
        }
        if(updateRoomDTO.getRoomType() != null){
            roomEntity.setRoomType(updateRoomDTO.getRoomType());
        }
        if(updateRoomDTO.getRoomState() != null){
            roomEntity.setRoomState(updateRoomDTO.getRoomState());
        }
        if(updateRoomDTO.getRoomCity() != null){
            roomEntity.setRoomCity(updateRoomDTO.getRoomCity());
        }
        if(updateRoomDTO.getRoomZip() != null){
            roomEntity.setRoomZip(updateRoomDTO.getRoomZip());
        }
        if(updateRoomDTO.getPricePerNight()!=0){
            roomEntity.setPricePerNight(updateRoomDTO.getPricePerNight());
        }
        if(updateRoomDTO.getRoomCapacity() != 0){
            roomEntity.setRoomCapacity(updateRoomDTO.getRoomCapacity());
        }

        if(newImg!=null && !newImg.isEmpty()){
            //First delete the old photo,
            //Store the new photo in aws
            //update the mainImgSrc
            String mainImgSrcKey = roomEntity.getCreatedByUser().getUserId()+"/"+roomEntity.getRoomId()+"/mainImg.jpg";
            String newUrl = s3Service.uploadFile(newImg, mainImgSrcKey);
            roomEntity.setMainImgSrc(newUrl);
        }
        roomRepository.save(roomEntity);
        return true;

    }

    @Override
    public List<GetRoomDTO> getAllRooms() {
        List<GetRoomDTO> rooms = new ArrayList<>();
        for(RoomEntity r: this.roomRepository.findAll()){
            rooms.add(new GetRoomDTO().makeGetRoomDTOFromRoomEntity(r));
        }
        return rooms;
    }

    @Override
    public Optional<GetRoomDTO> GetRoomById(Long roomId) {
        Optional<RoomEntity> roomEntity = this.roomRepository.findById(roomId);
        if(roomEntity.isEmpty()){
            throw new EntityNotFoundException("Room not found with id: "+roomId);
        }
        RoomEntity r = roomEntity.get();
        return Optional.of(new GetRoomDTO().makeGetRoomDTOFromRoomEntity(r));
    }

    @Override
    public List<GetRoomDTO> getAllRoomsAvailableBetweenDates(LocalDate startDate, LocalDate endDate) {
        List<RoomEntity> rooms = this.roomRepository.availableRoomsBetweenStartDateAndEndDate(startDate, endDate);
        List<GetRoomDTO> roomDTOs = new ArrayList<>();
        for(RoomEntity r: rooms){
            roomDTOs.add(new GetRoomDTO().makeGetRoomDTOFromRoomEntity(r));
        }
        return roomDTOs;
    }
    @Override
    public List<GetRoomDTO> getAllRoomsInZipCode(String zipCode) {
        List<RoomEntity> rooms = this.roomRepository.roomsInLocation(zipCode);
        List<GetRoomDTO> roomDTOs = new ArrayList<>();
        for(RoomEntity r: rooms){
            roomDTOs.add(new GetRoomDTO().makeGetRoomDTOFromRoomEntity(r));
        }
        return roomDTOs;
    }
    @Override
    public List<GetRoomDTO> getAllRoomsInZipCodeAndBetweenDates(String zipCode, LocalDate startDate, LocalDate endDate) {
        List<RoomEntity> rooms = this.roomRepository.roomsInLocationAvailableBetweenStartDateAndEndDate(zipCode, startDate, endDate);
        List<GetRoomDTO> roomDTOs = new ArrayList<>();
        for(RoomEntity r: rooms){
            roomDTOs.add(new GetRoomDTO().makeGetRoomDTOFromRoomEntity(r));
        }
        return roomDTOs;
    }
    @Override
    public List<GetRoomDTO> getAllRoomsByRoomType(String roomType) {
        List<RoomEntity> rooms = this.roomRepository.findByRoomType(roomType);
        List<GetRoomDTO> roomDTOs = new ArrayList<>();
        for(RoomEntity r: rooms){
            roomDTOs.add(new GetRoomDTO().makeGetRoomDTOFromRoomEntity(r));
        }
        return roomDTOs;
    }

    @Override
    public boolean isRoomAvailable(Long roomId, LocalDate startDate, LocalDate endDate) {
        this.roomRepository.findById(
                roomId
        ).orElseThrow(
                () -> new EntityNotFoundException("Room not found with id: "+roomId)
        );
        return this.roomRepository.roomAvailabilityBetweenStartDateAndEndDate(roomId, startDate, endDate) != null;
    }

}
