package de.thowl.notetilus.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import de.thowl.notetilus.core.services.AuthenticationService;
import de.thowl.notetilus.storage.entities.AccessToken;
import de.thowl.notetilus.web.forms.LoginForm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class AuthController {

	@Autowired
	private AuthenticationService authsvc;

	/**
	 * Shows the login page
	 * @return index.html
	 */
	@GetMapping("/login")
	public String showLoginPage() {
		log.info("entering showLoginPage (GET-Method: /login)");
		return "login";
	}

	/**
	 * Performs a login action
	 * @return index.html
	 */
	@PostMapping("/login")
	public String doLogin(LoginForm form, Model model) {
		log.info("entering doLogin (POST-Method: /login)");

		log.debug("Email = {}", form.getEmail());
		log.debug("Password = {}", form.getPassword());

		AccessToken token = authsvc.login(form.getEmail(),form.getPassword());

		if (null == token){
			// TODO: add localisation
			model.addAttribute("error", "E-Mail oder Passwort falsch");
			return "index";
		}

		model.addAttribute("user", token);
		//TODO: add dynamic route <Username>/notes/
		return "index";
	}
	
}
