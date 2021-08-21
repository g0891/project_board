package com.example.board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableFeignClients(basePackages = {"com.example.board.feign"})
public class BoardApplication {

	public static void main(String[] args) { SpringApplication.run(BoardApplication.class, args); }

}
