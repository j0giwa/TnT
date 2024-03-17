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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;

import de.thowl.tnt.core.exceptions.InvalidCredentialsException;
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
	 * @return login.html
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String showLoginPage() {

		log.info("entering showLoginPage (GET-Method: /login)");
		return "login";
	}

	/**
	 * Performs a login action
	 * 
	 * @return to dashboard page
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String doLogin(LoginForm form, Model model, HttpSession httpSession) {

		AccessToken token;
		String email = form.getEmail();
		String password = form.getPassword();

		log.info("entering doLogin (POST-Method: /login)");

		try {
			token = authsvc.login(email, password);
		} catch (InvalidCredentialsException e) {
			model.addAttribute("error", "E-Mail oder Passwort falsch");
			return "login";
		}

		User user = this.users.findByEmail(form.getEmail());

		httpSession.setAttribute("token", token);
		httpSession.setAttribute("username", user.getUsername());

		// NOTE: The username in URL the was the legacy user identifier,
		// kept due to the workload of changing it now.
		// Also looks kind of cool in the browser.
		return "redirect:/u/" + user.getUsername() + "/";
	}

	/**
	 * Performs a logout action
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String doLogout(@SessionAttribute("token") AccessToken token) {

		log.info(token.toString());
		authsvc.logout(token.getUsid());
		return "index";
	}

}
