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

import de.thowl.tnt.core.exceptions.DuplicateUserException;
import de.thowl.tnt.core.services.AuthenticationService;
import de.thowl.tnt.web.forms.RegisterForm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class RegisterController {

	@Autowired
	private AuthenticationService authsvc;

	/**
	 * Shows the register page
	 * 
	 * @return register.html
	 */
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String showRegisterPage() {

		log.info("entering showRegisterPage (GET-Method: /register)");
		return "register";
	}

	/**
	 * Registers a new user.
	 * 
	 * @return to register page
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String doRegister(RegisterForm form, Model model) {

		log.info("entering doRegister (POST-Method: /register)");

		if (!authsvc.validateEmail(form.getEmail())) {
			model.addAttribute("error", "email_error");
			return "register";
		}

		if (!authsvc.validatePassword(form.getPassword())) {
			model.addAttribute("error", "password_error");
			return "register";
		}

		if (!form.getPassword().equals(form.getPassword2())) {
			model.addAttribute("error", "password_match_error");
			return "register";
		}

		try {
			this.authsvc.register(form.getFirstname(), form.getLastname(), form.getUsername(),
					form.getEmail(),
					form.getPassword());
		} catch (DuplicateUserException e) {
			model.addAttribute("error", "duplicate_user_error");
		}

		return "register";
	}
}
