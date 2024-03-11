package de.thowl.tnt.web;

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
import de.thowl.tnt.storage.entities.NoteKategory;
import de.thowl.tnt.web.exceptions.ForbiddenException;
import de.thowl.tnt.web.forms.NoteForm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@SessionAttributes("notes")
public class SearchController {

	@Autowired
	private AuthenticationService authsvc;

	@Autowired
	private NotesService notessvc;

	@RequestMapping(value = "/u/{username}/search", method = RequestMethod.GET)
	public String findNotesByFilter(HttpServletRequest request,
			@SessionAttribute(name = "token", required = false) AccessToken token,
			@PathVariable("username") String username, NoteForm form, Model model) {

		log.info("entering showNotePage (GET-Method: /notes)");

		String query, referer;
		NoteKategory kategory;

		// Prevent unauthrised access / extend session
		if (!this.authsvc.validateSession(token, username))
			throw new ForbiddenException("Unathorised access");

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

		model.addAttribute("notes", this.notessvc.getNotesByParams(username, kategory, query));

		return "redirect:" + referer;
	}

}