package de.thowl.tnt.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;

import de.thowl.tnt.core.exceptions.NullUserException;
import de.thowl.tnt.core.services.AuthenticationService;
import de.thowl.tnt.storage.UserRepository;
import de.thowl.tnt.storage.entities.AccessToken;
import de.thowl.tnt.storage.entities.User;
import de.thowl.tnt.web.exceptions.ForbiddenException;
import de.thowl.tnt.web.forms.RegisterForm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ProfileController {

	@Autowired
	private AuthenticationService authsvc;

	@Autowired
	private UserRepository users;

	/**
	 * Shows the profile page
	 * 
	 * @return profile.html
	 */
	@RequestMapping(value = "/u/{username}/profile", method = RequestMethod.GET)
	public String showProfilePage(@SessionAttribute(name = "token", required = false) AccessToken token,
			@PathVariable("username") String username, Model model) {

		log.info("entering showProfilePage (GET-Method: /u/{username}/profile)");

		// Profildetails
		User user;

		// Prevent unauthrised access / extend session
		if (!this.authsvc.validateSession(token, username))
			throw new ForbiddenException("Unathorised access");

		user = this.users.findByUsername(username);

		model.addAttribute("user", user);
		return "profile";
	}

	@RequestMapping(value = "/u/{username}/profile", method = RequestMethod.POST)
	public String updateProfile(@SessionAttribute(name = "token", required = false) AccessToken token,
			@PathVariable("username") String username, RegisterForm form) {
		log.info("entering updateProfile (PSOT-Method: /u/{username}/profile)");

		User user;

		log.info(username);

		// Prevent unauthrised access / extend session
		if (!this.authsvc.validateSession(token, username))
			throw new ForbiddenException("Unathorised access");

		user = this.users.findByUsername(username);

		// Reload Userprofil
		try {
			this.authsvc.updateUser(user.getId(), form.getFirstname(), form.getLastname(),
					form.getUsername(),
					form.getEmail(), form.getPassword());
		} catch (NullUserException e) {

			log.error("User could not be updated");
		}

		return "redirect:/u/" + form.getUsername() + "/profile";
	}

}
