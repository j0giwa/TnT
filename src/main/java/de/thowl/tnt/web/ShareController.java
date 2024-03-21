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
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.server.ResponseStatusException;

import de.thowl.tnt.core.services.AuthenticationService;
import de.thowl.tnt.core.services.NotesService;
import de.thowl.tnt.storage.SharedNotesRepository;
import de.thowl.tnt.storage.entities.AccessToken;
import de.thowl.tnt.storage.entities.Note;
import de.thowl.tnt.storage.entities.SharedNote;
import de.thowl.tnt.storage.entities.User;
import de.thowl.tnt.web.forms.NoteForm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@SessionAttributes("shareurl")
public class ShareController {

	@Autowired
	private AuthenticationService authsvc;

	@Autowired
	private NotesService notesvc;

	@Autowired
	private SharedNotesRepository sharedNotes;

	@RequestMapping(value = "/share/{uuid}", method = RequestMethod.GET)
	public String showSharePage(@SessionAttribute(name = "token", required = false) AccessToken token, 
			@PathVariable("uuid") String uuid, Model model) {

		long id;
		Note note;
		User user;
		String username, avatar, mimetype;

		log.info("entering showSharePage (GET-Method: /share/{})", uuid);

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
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		id = this.notesvc.getSharedNote(uuid).getNote().getId();
		note = this.notesvc.getNote(id);

		model.addAttribute("user", username);
		model.addAttribute("avatar", avatar);
		model.addAttribute("avatarMimeType", mimetype);

		model.addAttribute("note", note);

		return "share";
	}

	@RequestMapping(value = "/u/{username}/share/getlink", method = RequestMethod.GET)
	public String getShareLink(HttpServletRequest request, SessionStatus status,
			@SessionAttribute(name = "token", required = false) AccessToken token, 
			@PathVariable("username") String username, NoteForm form, Model model) {

		User user;
		Note note;
		SharedNote sharedNote;
		String sharelink;
		String referer;

		log.info("entering getShareLink (GET-Method: /u/{}/share/getlink)", username);

		// Prevent unauthrised access
		if (!this.authsvc.validateSession(token, username))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Access Denied");

		this.authsvc.refreshSession(token);

		user = this.authsvc.getUserbySession(token);
		note = this.notesvc.getNote(form.getId(), user.getId());
		sharedNote = this.sharedNotes.findByNote(note);

		// If note is'nt shared yet, share it.
		if (null == sharedNote){
			toggleSharedNote(request, status, username, token, form, model);
			sharedNote = this.sharedNotes.findByNote(note);
		}

		sharelink = "/share/" + sharedNote.getGuid();
		model.addAttribute("shareurl", sharelink);

		referer = request.getHeader("Referer");
		return "redirect:" + referer;
	}

	@RequestMapping(value = "/u/{username}/share", method = RequestMethod.POST)
	public String toggleSharedNote(HttpServletRequest request, SessionStatus status,
			@PathVariable("username") String username, 
			@SessionAttribute(name = "token", required = false) AccessToken token,
			NoteForm form, Model model) {

		User user;
		Note note;
		SharedNote sharedNote;
		String guid, referer;

		log.info("entering toggleSharedNote (Post-Method: /u/{}/share)", username);

		// Prevent unauthrised access
		if (!this.authsvc.validateSession(token, username))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Access Denied");

		this.authsvc.refreshSession(token);

		user = this.authsvc.getUserbySession(token);

		if (form.getId() == 0) {
			String[] parts = form.getLink().split("/");
			guid = parts[2];
			sharedNote = this.sharedNotes.findByGuid(guid);
			note = sharedNote.getNote();
			this.notesvc.toggleSharing(note.getId(), user.getId());

			status.setComplete();
		} else {
			this.notesvc.toggleSharing(form.getId(), user.getId());
		}

		referer = request.getHeader("Referer");
		return "redirect:" + referer;
	}

	@RequestMapping(value = "/u/{username}/share/hideui", method = RequestMethod.POST)
	public String hidePopup(HttpServletRequest request, SessionStatus status,
			@PathVariable("username") String username, 
			@SessionAttribute(name = "token", required = false) AccessToken token) {

		String referer;

		log.info("entering hidePopup (Post-Method: /u/{username}/share/hideui)", username);

		// Prevent unauthrised access
		if (!this.authsvc.validateSession(token, username))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Access Denied");

		this.authsvc.refreshSession(token);

		status.setComplete();

		referer = request.getHeader("Referer");
		return "redirect:" + referer;	
	}

}
