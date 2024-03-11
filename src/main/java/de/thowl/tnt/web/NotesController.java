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
import de.thowl.tnt.web.exceptions.ForbiddenException;
import de.thowl.tnt.web.forms.NoteForm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@SessionAttributes("notes")
public class NotesController {

	@Autowired
	private AuthenticationService authsvc;

	@Autowired
	private NotesService notessvc;

	@RequestMapping(value = "/u/{username}/notes", method = RequestMethod.GET)
	public String showNotePage(@SessionAttribute(name = "token", required = false) AccessToken token,
			@PathVariable("username") String username, Model model) {
		log.info("entering showNotePage (GET-Method: /notes)");

		// Prevent unauthrised access / extend session
		if (!this.authsvc.validateSession(token, username))
			throw new ForbiddenException("Unathorised access");

		model.addAttribute("editing", false);
		model.addAttribute("user", username);

		if (!model.containsAttribute("notes"))
			model.addAttribute("notes", this.notessvc.getAllNotes(username));

		return "notes";
	}

	/**
	 * Adds a new note
	 * 
	 * @return todo.html
	 */
	@RequestMapping(value = "/u/{username}/notes", method = RequestMethod.POST)
	public String doAddNote(HttpServletRequest request, HttpSession httpSession,
			@SessionAttribute(name = "token", required = false) AccessToken token,
			@PathVariable("username") String username, NoteForm form, Model model) {

		log.info("entering doAddNote (POST-Method: /u/{}/notes)", username);

		String referer = request.getHeader("Referer");

		// Prevent unauthrised access / extend session
		if (!this.authsvc.validateSession(token, username))
			throw new ForbiddenException("Unathorised access");

		byte[] fileContent = null;
		String mimeType = "text/markdown";

		try {
			fileContent = form.getFile().getBytes();
			mimeType = form.getFile().getContentType();
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.notessvc.addNote(username, form.getTitle(), form.getSubtitle(), form.getContent(),
				fileContent, mimeType, form.getKategory(), form.getTags());

		return "redirect:" + referer;
	}

	/**
	 * Adds a new note
	 * 
	 * @return todo.html
	 */
	@RequestMapping(value = "/u/{username}/notes/edit", method = RequestMethod.GET)
	public String showEditPage(@SessionAttribute(name = "token", required = false) AccessToken token,
			@PathVariable("username") String username, NoteForm form, Model model,
			HttpSession httpSession) {
		log.info("entering showEditPage (POST-Method: /u/{}/notes/edit)", username);

		// Prevent unauthrised access / extend session
		if (!this.authsvc.validateSession(token, username))
			throw new ForbiddenException("Unathorised access");

		Note note = this.notessvc.getNote(form.getId());

		model.addAttribute("editing", true);
		model.addAttribute("noteTitle", note.getName());
		model.addAttribute("noteSubtitle", note.getSubtitle());
		model.addAttribute("noteContent", note.getContent());

		return "notes";
	}

	/**
	 * Adds a new note
	 * 
	 * @return todo.html
	 */
	@RequestMapping(value = "/u/{username}/notes/edit", method = RequestMethod.POST)
	public String doEditNote(@SessionAttribute(name = "token", required = false) AccessToken token,
			@PathVariable("username") String username, NoteForm form, Model model,
			HttpSession httpSession) {
		log.info("entering doAddNote (POST-Method: /u/{}/notes)", username);

		if (!this.authsvc.validateSession(token, username))
			throw new ForbiddenException("Unathorised access");

		byte[] fileContent = null;
		String mimeType = "text/markdown";

		try {
			fileContent = form.getFile().getBytes();
			mimeType = form.getFile().getContentType();
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.notessvc.editNote(form.getId(), username, form.getTitle(), form.getSubtitle(),
				form.getContent(), fileContent, mimeType, form.getKategory(), form.getTags());

		return "redirect:/u/" + username + "/notes";
	}

	/**
	 * Deletes a task
	 * 
	 * @return todo.html
	 */
	@RequestMapping(value = "/u/{username}/notes", method = RequestMethod.DELETE)
	public String doDeleteNote(HttpServletRequest request, HttpSession httpSession,
			@SessionAttribute(name = "token", required = false) AccessToken token,
			@PathVariable("username") String username, NoteForm form, Model model) {

		log.info("entering doDeleteNote (DELETE-Method: /u/{}/notes)", username);

		String referer = request.getHeader("Referer");

		// Prevent unauthrised access / extend session
		if (!this.authsvc.validateSession(token, username))
			throw new ForbiddenException("Unathorised access");

		this.notessvc.delete(form.getId());

		return "redirect:" + referer;
	}
}