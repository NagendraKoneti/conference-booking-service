package com.conference;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.conference.util.AppProperties;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *01/2024 
 * @author Nagendra
 */

@SpringBootApplication
@EnableSwagger2	
@EnableConfigurationProperties({AppProperties.class})
public class ConferenceRoomBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConferenceRoomBookingApplication.class, args);
	}

}
