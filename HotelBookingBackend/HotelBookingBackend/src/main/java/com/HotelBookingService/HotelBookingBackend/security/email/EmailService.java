package com.HotelBookingService.HotelBookingBackend.security.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    @Value("${spring.application.email.from-address}")
    private String mailFromAddress;


    @Async
    public void sendEmail(String to, String username, EmailTemplateName emailTemplate,
                          String confirmationUrl, String activationCode, String subject) throws MessagingException {

        String templateName;
        if(emailTemplate == null){
            templateName = "confirm-email";
        }else{
            templateName = emailTemplate.getName();
        }

        MimeMessage msg =mailSender.createMimeMessage();
        MimeMessageHelper msgHelper = new MimeMessageHelper(
                msg,MimeMessageHelper.MULTIPART_MODE_MIXED, StandardCharsets.UTF_8.name()
        );

        Map<String, Object> msgProperties = new HashMap<>();
        msgProperties.put("username", username);
        msgProperties.put("confirmationUrl", confirmationUrl);
        msgProperties.put("activationCode", activationCode);

        Context context = new Context(); //This is thymleaf context
        context.setVariables(msgProperties);



        msgHelper.setTo(to);
        msgHelper.setFrom(mailFromAddress);
        msgHelper.setSubject(subject);

        String template = templateEngine.process(templateName, context);
        msgHelper.setText(template, true);

        mailSender.send(msg);
    }

    @Async
    public void sendEmailWithProps(String to, EmailTemplateName emailTemplate,
                          Map<String, Object> msgInfo) throws MessagingException {

        //TODO: Throw exceptions if expected fields doesn't exist in msgInfos
        //For now, subject and user name are must

        List<String> msgAttributes = List.of("subject", "bookedBy",
                "roomName", "roomType", "roomCapacity",
                "roomPhone", "roomAddress", "roomState", "roomCity", "roomZip", "roomCountry",
                "roomImgSrc", "startDate", "endDate", "price");

        for(String att : msgAttributes){
            if(!(msgInfo.containsKey(att))){
                throw new MessagingException("Attribute " + att + " not found in msgInfo!");
            }
        }

        String templateName;
        if(emailTemplate == null){
            templateName = "confirm-email";
        }else{
            templateName = emailTemplate.getName();
        }

        MimeMessage msg =mailSender.createMimeMessage();
        MimeMessageHelper msgHelper = new MimeMessageHelper(
                msg,MimeMessageHelper.MULTIPART_MODE_MIXED, StandardCharsets.UTF_8.name()
        );
        
        Context context = new Context(); //This is thymleaf context
        context.setVariables(msgInfo);



        msgHelper.setTo(to);
        msgHelper.setFrom(mailFromAddress);
        msgHelper.setSubject(msgInfo.get("subject").toString());

        String template = templateEngine.process(templateName, context);
        msgHelper.setText(template, true);
        System.out.println("Email Sent!");
        mailSender.send(msg);
    }
}
