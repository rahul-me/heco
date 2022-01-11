package com.gcn.heco.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;



@SpringBootApplication
@ComponentScan(basePackages = "com.gcn.heco")
@EnableAsync
@EnableScheduling
public class HecoMain {
	
	// this is the main class to run spring boot project
	static Logger logger = LoggerFactory.getLogger(HecoMain.class);

	public static void main(String[] args) {
		try {
			SpringApplication.run(HecoMain.class, args);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}	
}
