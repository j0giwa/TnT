package de.thowl.tnt.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttribute;

import de.thowl.tnt.core.services.AuthenticationService;
import de.thowl.tnt.core.services.NotesService;
import de.thowl.tnt.core.services.TaskService;
import de.thowl.tnt.storage.entities.AccessToken;
import de.thowl.tnt.web.exceptions.ForbiddenException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class DashBoardcontroller {

	@Autowired
	private AuthenticationService authsvc;

	@Autowired
	private NotesService notessvc;

	@Autowired
	private TaskService tasksvc;

	@GetMapping("/u/{username}/")
	public String showNotePage(@SessionAttribute(name = "token", required = false) AccessToken token,
			@PathVariable("username") String username, Model model) {
		log.info("entering showNotePage (GET-Method: /notes)");

		// Prevent unauthrised access / extend session
		if (!this.authsvc.validateSession(token, username))
			throw new ForbiddenException("Unathorised access");

		model.addAttribute("user", username);

		model.addAttribute("notes", this.notessvc.getAllNotes(username));

		model.addAttribute("tasks", this.tasksvc.getAllTasks(username));

		return "dashboard";
	}

}
