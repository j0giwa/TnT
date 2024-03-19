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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

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
@SessionAttributes("noteSearchResults")
public class NotesController {

	@Autowired
	private AuthenticationService authsvc;

	@Autowired
	private NotesService notessvc;

	/**
	 * Shows the notes page
	 * 
	 * @return notes.html
	 */
	@RequestMapping(value = "/u/{username}/notes", method = RequestMethod.GET)
	public String showNotePage(@SessionAttribute(name = "token", required = false) AccessToken token,
			@PathVariable("username") String username, Model model) {

		long userId;
		User user;
		String avatar, mimetype;

		log.info("entering showNotePage (GET-Method: /u/{}/notes)", username);

		if (!this.authsvc.validateSession(token, username))
                	throw new AccessDeniedException("Access Forbidden");
			

		this.authsvc.refreshSession(token);

		user = this.authsvc.getUserbySession(token);
		userId = user.getId();

		avatar = user.getEncodedAvatar();
		mimetype = user.getMimeType();

		model.addAttribute("user", username);
		model.addAttribute("avatar", (avatar != null) ? avatar : "");
		model.addAttribute("avatarMimeType", (mimetype != null) ? mimetype : "");
		model.addAttribute("editing", false);

		if (model.containsAttribute("noteSearchResults")) {
			model.addAttribute("notes", model.getAttribute("noteSearchResults"));
		} else {
			model.addAttribute("notes", this.notessvc.getAllNotes(userId));
		}

		return "notes";
	}

	/**
	 * Adds a new note
	 * 
	 * @return to request origin
	 */
	@RequestMapping(value = "/u/{username}/notes", method = RequestMethod.POST)
	public String doAddNote(HttpServletRequest request,
			@SessionAttribute(name = "token", required = false) AccessToken token,
			@PathVariable("username") String username, NoteForm form, Model model) {

		long userId;
		User user;
		String referer, mimeType;
		byte[] fileContent;

		log.info("entering doAddNote (POST-Method: /u/{}/notes)", username);

		// Prevent unauthrised access / extend session
		if (!this.authsvc.validateSession(token, username))
			throw new AccessDeniedException("Access Forbidden");

		this.authsvc.refreshSession(token);

		user = this.authsvc.getUserbySession(token);
		userId = user.getId();

		// Fallbackvalues, they exist because they have to but don't realy matter
		fileContent = null;
		mimeType = "application/octet-stream";

		try {
			fileContent = form.getFile().getBytes();
			mimeType = form.getFile().getContentType();
		} catch (IOException e) {
			// No file was uploaded, this was probably intentional.
		}

		if (form.getTitle().isEmpty()) {
			model.addAttribute("error", "input_error");
		} else {
			this.notessvc.addNote(userId, form.getTitle(), form.getSubtitle(), form.getContent(),
					fileContent, mimeType, form.getKategory(), form.getTags());
		}

		referer = request.getHeader("Referer");
		return "redirect:" + referer;
	}

	/**
	 * Adds a new note
	 * 
	 * @return notes page
	 */
	@RequestMapping(value = "/u/{username}/notes/edit", method = RequestMethod.GET)
	public String showEditPage(@SessionAttribute(name = "token", required = false) AccessToken token,
			@PathVariable("username") String username, NoteForm form, Model model) {

		long userId;
		User user;
		Note note;
		String avatar, mimetype;

		log.info("entering showEditPage (POST-Method: /u/{}/notes/edit)", username);

		// Prevent unauthrised access / extend session
		if (!this.authsvc.validateSession(token, username))
			throw new AccessDeniedException("Access Forbidden");
		
		this.authsvc.refreshSession(token);

		user = this.authsvc.getUserbySession(token);
		userId = user.getId();

		avatar = user.getEncodedAvatar();
		mimetype = user.getMimeType();

		model.addAttribute("user", username);
		model.addAttribute("avatar", (avatar != null) ? avatar : "");
		model.addAttribute("avatarMimeType", (mimetype != null) ? mimetype : "");

		note = this.notessvc.getNote(form.getId(), userId);

		model.addAttribute("editing", true);
		model.addAttribute("noteTitle", note.getName());
		model.addAttribute("noteSubtitle", note.getSubtitle());
		model.addAttribute("noteContent", note.getContent());
		model.addAttribute("noteTags", String.join(" ", note.getTags()));

		return "notes";
	}

	/**
	 * Adds a new note
	 * 
	 * @return to notes page
	 */
	@RequestMapping(value = "/u/{username}/notes/edit", method = RequestMethod.POST)
	public String doEditNote(@SessionAttribute(name = "token", required = false) AccessToken token,
			@PathVariable("username") String username, NoteForm form, Model model) {

		long userId;
		User user;
		byte[] fileContent;
		String mimeType;

		log.info("entering doAddNote (POST-Method: /u/{}/notes)", username);

		if (!this.authsvc.validateSession(token, username))
			throw new AccessDeniedException("Access Forbidden");

		this.authsvc.refreshSession(token);

		user = this.authsvc.getUserbySession(token);
		userId = user.getId();

		// Fallbackvalues, they exist because they have to but don't realy matter
		fileContent = null;
		mimeType = "application/octet-stream";

		try {
			fileContent = form.getFile().getBytes();
			mimeType = form.getFile().getContentType();
		} catch (IOException e) {
			// No file was uploaded, this was probably intentional.
		}

		if (form.getTitle().isEmpty()) {
			model.addAttribute("error", "input_error");
		} else {
			this.notessvc.editNote(form.getId(), userId,  form.getTitle(), form.getSubtitle(), 
					form.getContent(), fileContent, mimeType, form.getKategory(), form.getTags());
		}

		return "redirect:/u/" + username + "/notes";
	}

	/**
	 * Deletes a note
	 * 
	 * @return to request origin
	 */
	@RequestMapping(value = "/u/{username}/notes", method = RequestMethod.DELETE)
	public String doDeleteNote(HttpServletRequest request,
			@SessionAttribute(name = "token", required = false) AccessToken token,
			@PathVariable("username") String username, NoteForm form, Model model) {

		long userId;
		User user;
		String referer;

		log.info("entering doDeleteNote (DELETE-Method: /u/{}/notes)", username);

		// Prevent unauthrised access
		if (!this.authsvc.validateSession(token, username))
			throw new AccessDeniedException("Access Forbidden");

		this.authsvc.refreshSession(token);

		user = this.authsvc.getUserbySession(token);
		userId = user.getId();

		this.notessvc.delete(form.getId(), userId);

		referer = request.getHeader("Referer");
		return "redirect:" + referer;
	}
}
