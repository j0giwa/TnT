package de.thowl.tnt.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;

import de.thowl.tnt.core.services.AuthenticationService;
import de.thowl.tnt.storage.entities.AccessToken;
import de.thowl.tnt.web.exceptions.ForbiddenException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class CalendarController {

	@Autowired
	private AuthenticationService authsvc;

	/**
	 * Shows the calendar page
	 * 
	 * @return calendar.html
	 */
	@RequestMapping(value = "/u/{username}/calendar", method = RequestMethod.GET)
	public String showCalendarPage(@SessionAttribute(name = "token", required = false) AccessToken token,
			@PathVariable("username") String username, Model model) {

		log.info("entering showCalendarPage (GET-Method: /u/{username}/calendar)");

		// Prevent unauthrised access
		if (!this.authsvc.validateSession(token, username))
			throw new ForbiddenException("Unathorised access");

		this.authsvc.refreshSession(token);

		model.addAttribute("user", username);

		return "calendar";
	}

}
