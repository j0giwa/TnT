package de.thowl.notetilus.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class NotesController {

	@GetMapping("/u/{username}/notes")
	public String showLoginPage() {
		log.info("entering showLoginPage (GET-Method: /login)");
		return "notes";
	}
}
