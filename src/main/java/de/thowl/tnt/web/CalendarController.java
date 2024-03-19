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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;

import de.thowl.tnt.core.services.AuthenticationService;
import de.thowl.tnt.storage.entities.AccessToken;
import de.thowl.tnt.storage.entities.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class CalendarController {

	@Autowired
	private AuthenticationService authsvc;

	/**
	 * Shows the calendar page
	 * 
	 * @return calendar.html
	 */
	@RequestMapping(value = "/u/{username}/calendar", method = RequestMethod.GET)
	public String showCalendarPage(@SessionAttribute(name = "token", required = false) AccessToken token,
			@PathVariable("username") String username, Model model) {

		User user;
		String avatar, mimetype;

		log.info("entering showCalendarPage (GET-Method: /u/{username}/calendar)");

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

		return "calendar";
	}

}
