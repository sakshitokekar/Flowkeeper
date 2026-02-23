package com.example.Flowkeeper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FlowkeeperApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlowkeeperApplication.class, args);
	}

}
