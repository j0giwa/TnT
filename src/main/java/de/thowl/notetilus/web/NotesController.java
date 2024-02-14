package de.thowl.notetilus.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import de.thowl.notetilus.core.services.AuthenticationService;
import de.thowl.notetilus.storage.entities.AccessToken;
import de.thowl.notetilus.storage.entities.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class NotesController {

	@Autowired
	private AuthenticationService authsvc;

	@GetMapping("/u/{username}/notes")
	public String showNotePage(@SessionAttribute(name = "user", required = false) User user, @SessionAttribute(name = "token", required = false) AccessToken token) {
		log.info("entering showLoginPage (GET-Method: /login)");

		// Prevent unauthrised access
		if (!this.authsvc.validateSession(user, token))
			throw new ForbiddenException("Unathorised access"); // Custom exception

		return "notes";
	}
}
