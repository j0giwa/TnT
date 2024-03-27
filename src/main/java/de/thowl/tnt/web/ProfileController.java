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

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.server.ResponseStatusException;

import de.thowl.tnt.core.exceptions.NullUserException;
import de.thowl.tnt.core.services.AuthenticationService;
import de.thowl.tnt.storage.entities.AccessToken;
import de.thowl.tnt.storage.entities.User;
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

		log.info("entering showProfilePage (GET-Method: /u/{}/profile)", username);

		// Prevent unauthrised access
		if (!this.authsvc.validateSession(token, username))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Access Denied");

		this.authsvc.refreshSession(token);

		user = this.authsvc.getUserbySession(token);

		model.addAttribute("user", user);

		if (user.getAvatar() != null) {
			model.addAttribute("avatar", user.getEncodedAvatar());
			model.addAttribute("avatarMimeType", user.getEncodedAvatar());
		} else {
			model.addAttribute("avatar", "");
			model.addAttribute("avatarMimeType", "");
		}
	
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
		String mimeType;
		byte[] fileContent;

		log.info("entering updateProfile (POST-Method: /u/{}/profile)", username);

		// Prevent unauthrised access
		if (!this.authsvc.validateSession(token, username))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Access Denied");

		this.authsvc.refreshSession(token);

		user = this.authsvc.getUserbySession(token);

		if (!authsvc.validateEmail(form.getEmail())) {
			model.addAttribute("error", "email_error");
			return "redirect:/u/" + form.getUsername() + "/profile";
		}

		if (!authsvc.validatePassword(form.getPassword())) {
			model.addAttribute("error", "password_error");
			return "redirect:/u/" + form.getUsername() + "/profile";
		}

		if (!form.getPassword().equals(form.getPassword2())) {
			model.addAttribute("error", "password_match_error");
			return "redirect:/u/" + form.getUsername() + "/profile";
		}

		fileContent = null;
		mimeType = "application/octet-stream";

		try {
			fileContent = form.getAvatar().getBytes();
			mimeType = form.getAvatar().getContentType();
		} catch (IOException e) {
			log.error("Cannot update avatar without a file");
		}

		try {
			this.authsvc.updateUser(user.getId(), form.getFirstname(), form.getLastname(),
					form.getUsername(),
					form.getEmail(), form.getPassword(), fileContent, mimeType);
		} catch (NullUserException e) {

			log.error("User could not be updated");
		}

		return "redirect:/u/" + form.getUsername() + "/profile";
	}

}
