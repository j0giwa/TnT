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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;

import de.thowl.tnt.core.services.AuthenticationService;
import de.thowl.tnt.core.services.NotesService;
import de.thowl.tnt.storage.entities.AccessToken;
import de.thowl.tnt.storage.entities.Note;
import de.thowl.tnt.storage.entities.User;
import de.thowl.tnt.web.forms.NoteForm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ShareController {

	@Autowired
	private AuthenticationService authsvc;

	@Autowired
	private NotesService notesvc;

	@RequestMapping(value = "/share/{uuid}", method = RequestMethod.GET)
	public String showSharePage(@SessionAttribute(name = "token", required = false) AccessToken token, 
			@PathVariable("uuid") String uuid, Model model) {

		long id;
		Note note;
		User user;
		String username, avatar, mimetype;

		log.info("entering showSharePage (GET-Method: /share)");

		// Fallbackvalues, beeing looged is is optional
		username = "Guest";
		avatar = "";
		mimetype = "";

		try {
			user = this.authsvc.getUserbySession(token);
			username = user.getUsername();
			avatar = user.getEncodedAvatar();
			mimetype = user.getMimeType();
		} catch (NullPointerException e) {
			// TODO: handle exception
		}
		
		id = this.notesvc.getSharedNote(uuid).getNote().getId();
		note = this.notesvc.getNote(id);

		model.addAttribute("user", username);
		model.addAttribute("avatar", avatar);
		model.addAttribute("avatarMimeType", mimetype);

		model.addAttribute("note", note);

		return "share";
	}

	@RequestMapping(value = "/share", method = RequestMethod.POST)
	public String addSharedNote(HttpServletRequest request,
			@SessionAttribute(name = "token", required = false) AccessToken token,
			NoteForm form, Model model) {

		long userId;
		User user;
		String referer;

		log.info("entering addSharedNote (Post-Method: /share)");

		user = this.authsvc.getUserbySession(token);
		userId = user.getId();

		this.notesvc.toggleSharing(form.getId(), userId);

		referer = request.getHeader("Referer");
		return "redirect:" + referer;
	}

}
