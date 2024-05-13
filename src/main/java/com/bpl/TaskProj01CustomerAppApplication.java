package com.bpl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TaskProj01CustomerAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskProj01CustomerAppApplication.class, args);
	}

}
