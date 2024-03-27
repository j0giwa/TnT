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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.server.ResponseStatusException;

import de.thowl.tnt.core.services.AuthenticationService;
import de.thowl.tnt.core.services.NotesService;
import de.thowl.tnt.storage.entities.AccessToken;
import de.thowl.tnt.storage.entities.NoteKategory;
import de.thowl.tnt.storage.entities.User;
import de.thowl.tnt.web.forms.NoteForm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@SessionAttributes("noteSearchResults")
public class SearchController {

	@Autowired
	private AuthenticationService authsvc;

	@Autowired
	private NotesService notessvc;

	@RequestMapping(value = "/u/{username}/search", method = RequestMethod.GET)
	public String findNotesByFilter(HttpServletRequest request,
			@SessionAttribute(name = "token", required = false) AccessToken token,
			@PathVariable("username") String username, NoteForm form, Model model) {

		long userId;
		User user;
		String query, referer;
		NoteKategory kategory;

		log.info("entering showNotePage (GET-Method: /u/{}/search)", username);

		// Prevent unauthrised access
		if (!this.authsvc.validateSession(token, username))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Access Denied");

		this.authsvc.refreshSession(token);

		user = this.authsvc.getUserbySession(token);
		userId = user.getId();

		query = form.getQuery();

		switch (form.getKategory().toLowerCase()) {
			default:
			case "all":
				kategory = NoteKategory.ALL;
				break;
			case "lecture":
				kategory = NoteKategory.LECTURE;
				break;
			case "litterature":
				kategory = NoteKategory.LITTERATURE;
				break;
			case "misc":
				kategory = NoteKategory.MISC;
				break;
		}

		referer = request.getHeader("Referer");

		model.addAttribute("noteSearchResults", this.notessvc.getNotesByParams(userId, kategory, query));

		return "redirect:" + referer;
	}

}
