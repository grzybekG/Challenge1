package com.challenge1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;

@SpringBootApplication
public class ReadStreamApplication{

	public static void main(String[] args) {
		SpringApplication.run(ReadStreamApplication.class, args);
	}
}
