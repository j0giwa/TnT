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

	/**
	 * Shows the profile page
	 * 
	 * @return profile.html
	 */
	@RequestMapping(value = "/u/{username}/profile", method = RequestMethod.GET)
	public String showProfilePage(@SessionAttribute(name = "token", required = false) AccessToken token,
			@PathVariable("username") String username, Model model) {

		User user;

		log.info("entering showProfilePage (GET-Method: /u/{username}/profile)");

		// Prevent unauthrised access / extend session
		if (!this.authsvc.validateSession(token, username))
			throw new ForbiddenException("Unathorised access");

		user = this.authsvc.getUserbySession(token);

		model.addAttribute("user", user);
		return "profile";
	}

	/**
	 * Upadate the user profile
	 * 
	 * @return to profile page
	 */
	@RequestMapping(value = "/u/{username}/profile", method = RequestMethod.POST)
	public String updateProfile(@SessionAttribute(name = "token", required = false) AccessToken token,
			@PathVariable("username") String username, RegisterForm form, Model model) {

		User user;

		log.info("entering updateProfile (POST-Method: /u/{username}/profile)");

		// Prevent unauthrised access / extend session
		if (!this.authsvc.validateSession(token, username))
			throw new ForbiddenException("Unathorised access");

		user = this.authsvc.getUserbySession(token);

		if (!authsvc.validateEmail(form.getEmail()))
			model.addAttribute("error", "email_error");

		if (!authsvc.validatePassword(form.getPassword()))
			model.addAttribute("error", "password_error");

		if (!form.getPassword().equals(form.getPassword2()))
			model.addAttribute("error", "password_match_error");

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
