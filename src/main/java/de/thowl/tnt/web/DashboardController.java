package de.thowl.tnt.web;

import java.util.ArrayList;
import java.util.List;

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
import de.thowl.tnt.core.services.TaskService;
import de.thowl.tnt.storage.entities.AccessToken;
import de.thowl.tnt.storage.entities.Priority;
import de.thowl.tnt.storage.entities.Task;
import de.thowl.tnt.storage.entities.User;
import de.thowl.tnt.web.exceptions.ForbiddenException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@SessionAttributes("noteSearchResults")
public class DashboardController {

	@Autowired
	private AuthenticationService authsvc;

	@Autowired
	private NotesService notessvc;

	@Autowired
	private TaskService tasksvc;

	/**
	 * Shows the calendar page
	 * 
	 * @return dashboard.html
	 */
	@RequestMapping(value = "/u/{username}/", method = RequestMethod.GET)
	public String showDashbardPage(HttpServletRequest request,
			@SessionAttribute(name = "token", required = false) AccessToken token,
			@PathVariable("username") String username, Model model) {

		long userId;
		User user;
		List<Task> tasks;

		log.info("entering showDashboardPage (GET-Method: /u/{username}/)");

		// Prevent unauthrised access
		if (!this.authsvc.validateSession(token, username))
			throw new ForbiddenException("Unathorised access");

		this.authsvc.refreshSession(token);

		user = this.authsvc.getUserbySession(token);
		userId = user.getId();

		model.addAttribute("user", username);
		model.addAttribute("avatar", user.getEncodedAvatar());
		model.addAttribute("avatarMimeType", user.getMimeType());
		
		tasks = new ArrayList<Task>();
		tasks.addAll(this.tasksvc.getAllOverdueTasks(userId));
		tasks.addAll(this.tasksvc.getTasksByPriority(userId, Priority.HIGH));

		model.addAttribute("tasks", tasks);

		if (model.containsAttribute("noteSearchResults")) {

			model.addAttribute("notes", model.getAttribute("noteSearchResults"));
			// TODO: This had not the desired effect, reset button nessesary
			// request.getSession().removeAttribute("noteSearchResults");
		} else {
			model.addAttribute("notes", this.notessvc.getAllNotes(userId));
		}

		return "dashboard";
	}

}
