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

import de.thowl.tnt.core.services.AuthenticationService;
import de.thowl.tnt.web.forms.RegisterForm;
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

	@PostMapping("/register")
	public String doRegister(RegisterForm form, Model model) {
		log.info("entering doRegister (POST-Method: /register)");

		if (!authsvc.validateEmail(form.getEmail()))
			model.addAttribute("error", "email_error");

		if (!authsvc.validatePassword(form.getPassword()))
			model.addAttribute("error", "password_error");

		if (!form.getPassword().equals(form.getPassword2()))
			model.addAttribute("error", "password_match_error");

		this.authsvc.register(form.getFirstname(), form.getLastname(), form.getUsername(), form.getEmail(),
				form.getPassword());

		return "register";
	}
}
