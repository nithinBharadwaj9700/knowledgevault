package com.example.knowledgevault;


import com.example.knowledgevault.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class KnowledgevaultApplication {

	public static void main(String[] args) {
		SpringApplication.run(KnowledgevaultApplication.class, args);
	}
}