package com.HotelBookingService.HotelBookingBackend;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
@RequiredArgsConstructor
public class HotelBookingBackendApplication {



	public static void main(String[] args) {

		SpringApplication.run(HotelBookingBackendApplication.class, args);
	}
}
