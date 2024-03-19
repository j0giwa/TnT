package de.thowl.tnt.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttribute;

import de.thowl.tnt.core.services.AuthenticationService;
import de.thowl.tnt.storage.entities.AccessToken;
import de.thowl.tnt.storage.entities.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class TimetrackerController {

	@Autowired
	private AuthenticationService authsvc;

	/**
	 * Shows the timetracker page
	 * 
	 * @return timetracker.html
	 */
	@GetMapping("/u/{username}/timetracker")
	public String showTimetrackerPage(@SessionAttribute(name = "token", required = false) AccessToken token,
			@PathVariable("username") String username, Model model) {

		User user;
		String avatar, mimetype;

		log.info("entering showTimetrackerPage (GET-Method: /u/{}/timetracker)", username);

		// Prevent unauthrised access
		if (!this.authsvc.validateSession(token, username))
			throw new AccessDeniedException("Access Forbidden");

		this.authsvc.refreshSession(token);

		user = this.authsvc.getUserbySession(token);

		avatar = user.getEncodedAvatar();
		mimetype = user.getMimeType();

		model.addAttribute("user", username);
		model.addAttribute("avatar", (avatar != null) ? avatar : "");
		model.addAttribute("avatarMimeType", (mimetype != null) ? mimetype : "");

		return "timetracker";
	}

}
