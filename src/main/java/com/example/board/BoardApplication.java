package com.example.board;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BoardApplication {

	@Bean
	public Logger getLogger() {
		return LoggerFactory.getLogger(BoardApplication.class);
	}

	public static void main(String[] args) { SpringApplication.run(BoardApplication.class, args); }

}
