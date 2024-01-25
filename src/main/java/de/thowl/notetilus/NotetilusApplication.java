package de.thowl.notetilus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class NotetilusApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotetilusApplication.class, args);
	}

}
