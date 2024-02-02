package de.thowl.prog3.api.web.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teapot")
public class TeaPotService {

	@GetMapping("/")
	public ResponseEntity<String> teapot() {
		return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body("IM A TEAPOT");
	}
}
