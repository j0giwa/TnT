/*
 *  TnT, Todo's 'n' Texts
 *  Copyright (C) 2023  <name of author>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package de.thowl.tnt.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import de.thowl.tnt.core.exeptions.InvalidCredentialsException;
import de.thowl.tnt.core.services.AuthenticationService;
import de.thowl.tnt.storage.UserRepository;
import de.thowl.tnt.storage.entities.AccessToken;
import de.thowl.tnt.storage.entities.User;
import de.thowl.tnt.web.forms.LoginForm;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class AuthController {

	@Autowired
	private AuthenticationService authsvc;
	@Autowired
	private UserRepository users;

	/**
	 * Shows the login page
	 * 
	 * @return index.html
	 */
	@GetMapping("/login")
	public String showLoginPage() {
		log.info("entering showLoginPage (GET-Method: /login)");
		return "login";
	}

	/**
	 * Performs a login action
	 * 
	 * @return index.html
	 */
	@PostMapping("/login")
	public String doLogin(LoginForm form, Model model, HttpSession httpSession) {
		log.info("entering doLogin (POST-Method: /login)");

		AccessToken token;
		String email = form.getEmail();
		String password = form.getPassword();

		try {
			token = authsvc.login(email, password);
		} catch (InvalidCredentialsException e) {
			model.addAttribute("error", "E-Mail oder Passwort falsch");
			return "login";
		}

		// TODO: nit: would probably be better if we get the user via the token.
		User user = this.users.findByEmail(form.getEmail());

		httpSession.setAttribute("token", token);
		httpSession.setAttribute("username", user.getUsername());

		return "redirect:/u/" + user.getUsername() + "/notes";
	}

	/**
	 * Performs a logout action
	 */
	// NOTE: GetMapping was easier than handling this via a post request
	@GetMapping("/logout")
	public String doLogout(@SessionAttribute("token") AccessToken token) {

		log.info(token.toString());
		authsvc.logout(token.getUsid());
		return "index";
	}

}
