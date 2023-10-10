package com.github.tehsteel.tpunishments.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, UserDetailsServiceAutoConfiguration.class})
@ComponentScan
@EnableMongoRepositories
@RestController
public class PunishmentsWebApplication {


	@Autowired
	private Environment environment;

	public static void main(final String[] args) {
		new PunishmentsWebApplication().startApp(args);
	}

	private void startApp(final String[] args) {
		SpringApplication.run(PunishmentsWebApplication.class, args);
	}

	@GetMapping("/test")
	public ResponseEntity<String> testRequest() {
		return new ResponseEntity<>("OK", HttpStatus.OK);
	}

	@Bean
	ApplicationRunner applicationRunner() {
		return args -> Constants.API_KEY = environment.getProperty("spring.security.apikey");
	}
}
