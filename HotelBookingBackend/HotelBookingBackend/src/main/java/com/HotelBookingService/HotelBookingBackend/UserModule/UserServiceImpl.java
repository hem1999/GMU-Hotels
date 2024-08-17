package com.HotelBookingService.HotelBookingBackend.UserModule;

import com.HotelBookingService.HotelBookingBackend.BookingModule.DTOs.GetBookingDTO;
import com.HotelBookingService.HotelBookingBackend.UserModule.DTOs.GetUserDTO;
import com.HotelBookingService.HotelBookingBackend.UserModule.DTOs.updateUserDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserServices {


    private final UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    @Override
    public GetUserDTO findUserByEmail(String email) {
        Optional<UserEntity> user = userRepository.findByEmail(email);
        if(user.isEmpty()){
            throw new EntityNotFoundException("User not found with email" + email);
        }
        return new GetUserDTO().makeGetUserDTOFromEntity(user.get());
    }

    @Override
    public GetUserDTO findUserByUsername(String username) {
        Optional<UserEntity> user = userRepository.findByUsername(username);
        if(user.isEmpty()){
            throw new EntityNotFoundException("User not found with username" + username);
        }
        return new GetUserDTO().makeGetUserDTOFromEntity(user.get());
    }

    @Override
    public GetUserDTO findUserById(Long id) {
        Optional<UserEntity> user = this.userRepository.findById(id);
        if(user.isEmpty()){
            throw new EntityNotFoundException("User not found with id " + id);
        }
        return new GetUserDTO().makeGetUserDTOFromEntity(user.get());
    }


    @Override
    public boolean updateUser(updateUserDTO userDTO) {
            Optional<UserEntity> oldUserEntity = this.userRepository.findById(userDTO.getUserId());
            if(oldUserEntity.isPresent()){
                UserEntity oldUser = oldUserEntity.get();
                if(userDTO.getFirstName() != null) {
                    oldUser.setFirstName(userDTO.getFirstName());
                }
                if(userDTO.getLastName()!=null) {
                    oldUser.setLastName(userDTO.getLastName());
                }
                if(userDTO.getPhone()!=null) {
                    oldUser.setPhone(userDTO.getPhone());
                }
                if(userDTO.getAddress()!=null) {
                    oldUser.setAddress(userDTO.getAddress());
                }
                if(userDTO.getCity()!=null) {
                    oldUser.setCity(userDTO.getCity());
                }
                if(userDTO.getState()!=null) {
                    oldUser.setState(userDTO.getState());
                }
                if(userDTO.getZip()!=null) {
                    oldUser.setZip(userDTO.getZip());
                }

                if (userDTO.getCountry()!=null) {
                    oldUser.setCountry(userDTO.getCountry());
                }


                    this.userRepository.save(oldUser);
                return true;
            }else{
                throw new EntityNotFoundException("User not found");
            }

    }

    @Override
    public boolean deleteUser(Long userId) {
        Optional<UserEntity> user = this.userRepository.findById(userId);
        if(user.isPresent()){
            this.userRepository.delete(user.get());
            return true;
        }
        return false;
    }

    @Override
    public Map<String, List<GetBookingDTO>> getBookings(Long userId) {
        Optional<UserEntity> ue  = this.userRepository.findById(userId);
        if(ue.isPresent()){
            UserEntity user = ue.get();
            return new GetUserDTO().makeGetUserDTOFromEntity(user).getBookings();
        }
        Map<String, List<GetBookingDTO>> bookings = new HashMap<>();
        bookings.put("previous", new ArrayList<>());
        bookings.put("current", new ArrayList<>());
        bookings.put("upcoming", new ArrayList<>());
        return bookings;
    }
}
