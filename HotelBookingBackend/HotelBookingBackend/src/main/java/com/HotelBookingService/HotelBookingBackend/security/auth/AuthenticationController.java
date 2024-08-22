package com.HotelBookingService.HotelBookingBackend.security.auth;

import com.HotelBookingService.HotelBookingBackend.security.jwt.JwtService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authService;
    private final JwtService jwtService;

    @PostMapping("/validateToken")
    public ResponseEntity<?> validateToken(@RequestBody ValidateRequestDTO validateRequestDTO) {
        if(!jwtService.isTokenExpired(validateRequestDTO.getJwtToken())){
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @RequestBody @Valid RegistrationRequestDTO request
    ) throws MessagingException {
        authService.register(request);
        return ResponseEntity.accepted().build();

    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDTO> authenticateUser(
            @RequestBody @Valid AuthenticationRequestDTO request
    ){
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @GetMapping("/activate-account")
    public ResponseEntity<?> activateAccount(
            @RequestParam String activationCode
    ) throws MessagingException {
        Map<String, String> map = new HashMap<>();

        try{
            authService.activateAccount(activationCode);
            map.put("message","account activated");
            return new ResponseEntity<>(map, HttpStatus.OK);

        }catch (Exception e){
            map.put("message",e.getMessage());
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
    }
}
