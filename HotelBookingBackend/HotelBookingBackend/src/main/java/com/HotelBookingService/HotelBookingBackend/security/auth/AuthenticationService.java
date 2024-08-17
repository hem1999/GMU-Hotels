package com.HotelBookingService.HotelBookingBackend.security.auth;

import com.HotelBookingService.HotelBookingBackend.UserModule.UserEntity;
import com.HotelBookingService.HotelBookingBackend.UserModule.UserRepository;
import com.HotelBookingService.HotelBookingBackend.UserModule.role.RoleRepository;
import com.HotelBookingService.HotelBookingBackend.UserModule.token.Token;
import com.HotelBookingService.HotelBookingBackend.UserModule.token.TokenRepository;
import com.HotelBookingService.HotelBookingBackend.security.email.EmailService;
import com.HotelBookingService.HotelBookingBackend.security.email.EmailTemplateName;
import com.HotelBookingService.HotelBookingBackend.security.jwt.JwtService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager; // From the jwt.BeansConfig
    private final JwtService jwtService;

    @Value("${spring.app.activationUrl}")
    private String activationUrl;

    public void register(RegistrationRequestDTO request) throws MessagingException {
        //Seeing if there exists a role with the asked userType by the user!
        //Else throwing the runtime exception.
        var userRole =roleRepository.findByName(request.getUserType()).orElseThrow(
                () -> new RuntimeException("User type not found")
        );


        //Building the UserEntity with builder `Which is available due to lombok annotation @Builder
        // on UserEntity`.
        //Right now the enabled attribute of the account is still false
        //It will be changed to true once they activate by verifying the email.
        var user = UserEntity.builder().firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .country(request.getCountry())
                .zip(request.getZip())
                .phone(request.getPhone())
                .userType(request.getUserType())
                .createdDate(LocalDateTime.now())
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();
        //Saving the userEntity object to the database
        userRepository.save(user);
        //Calling the sendValidationEmail which sends the email for activation.
        sendValidationEmail(user);

    }

    private void sendValidationEmail(UserEntity user) throws MessagingException {
        //First we have to generate a token(like a OTP) for sending it to mail
        //They can use that to activate the account.
        var newActivationToken = generateAndSaveActivationToken(user);
        //Need to send the email.
        System.out.println("Calling the sendEmail service with"+ user.getUsername() + " code: "+newActivationToken);
        emailService.sendEmail(user.getEmail(), user.getUsername(),  EmailTemplateName.ACTIVATE_ACCOUNT, activationUrl,
                newActivationToken, "Account Activation"
        );


    }

    private String generateAndSaveActivationToken(UserEntity user) {
        //Calling the generateToken which generates a random OTP of length 6.
        String generatedToken = generateActivationToken(6);
        //Using Token entity to build with the above generated token and then saving it to database.
        //We are only giving it 15Mins for expiration.
        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user).build();
        //saving to repository.
        tokenRepository.save(token);
        //returning the generated token.
        return generatedToken;
    }

    private String generateActivationToken(int length) {
        //This is for the OTP Service.
        String posChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; //These are the posChars taken to generate the random string
        StringBuilder token = new StringBuilder();
        //We are using secureRandom to generate the random index for using a char
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            //constructing the token using the charsAt(i) where i is the random index in the range of above posChars length
            token.append(posChars.charAt(random.nextInt(posChars.length())));
        }
        //return the string.
        return token.toString();
    }

    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        var claims = new HashMap<String, Object>();
        var user = (UserEntity) auth.getPrincipal();
        claims.put("email", user.getEmail());
        var jwtToken = jwtService.generateToken(claims, user);
        return AuthenticationResponseDTO.builder()
                .jwtToken(jwtToken)
                .userId(user.getUserId())
                .userType(user.getUserType())
                .build();
    }

    public void activateAccount(String activationCode) throws MessagingException {
        Token savedActivationToken = tokenRepository.findByToken(activationCode).orElseThrow(
                ()-> new RuntimeException("Activation code not found")
        );
        if(LocalDateTime.now().isAfter(savedActivationToken.getExpiresAt())) {
            sendValidationEmail(savedActivationToken.getUser());
            throw new RuntimeException("Activation code expired, new one sent to same address!");
        }


        var user  = userRepository.findById(savedActivationToken.getUser().getUserId()).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );

        user.setEnabled(true);
        userRepository.save(user);

        savedActivationToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedActivationToken);
    }
}
