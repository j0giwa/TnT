package de.thowl.notetilus.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import de.thowl.notetilus.core.exeptions.InvalidCredentialsException;
import de.thowl.notetilus.core.services.AuthenticationService;
import de.thowl.notetilus.web.forms.RegisterForm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class RegisterController {

	@Autowired
	private AuthenticationService authsvc;

	@GetMapping("/register")
	public String showRegisterPage() {
		log.info("entering showRegisterPage (GET-Method: /register)");
		return "register";
	}

	@PostMapping("/signup")
	public String doRegister(RegisterForm form, Model model) {
		log.info("entering doRegister (POST-Method: /register)");

		try {
			authsvc.register(form.getFirstname(), form.getLastname(), form.getUsername(), form.getEmail(), form.getPassword(), form.getPassword2());
		} catch (InvalidCredentialsException e) {
			model.addAttribute("error", "E-Mail oder Passwort ungültig");
			return "register";
		}

		return "register";
	}
}
