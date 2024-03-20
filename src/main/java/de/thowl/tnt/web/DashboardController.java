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

import java.util.ArrayList;
import java.util.List;

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
import de.thowl.tnt.core.services.TaskService;
import de.thowl.tnt.storage.entities.AccessToken;
import de.thowl.tnt.storage.entities.Priority;
import de.thowl.tnt.storage.entities.Task;
import de.thowl.tnt.storage.entities.User;
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
		String avatar, mimetype;

		log.info("entering showDashboardPage (GET-Method: /u/{}/)", username);

		// Prevent unauthrised access
		if (!this.authsvc.validateSession(token, username))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Access Denied");

		this.authsvc.refreshSession(token);

		user = this.authsvc.getUserbySession(token);
		userId = user.getId();

		avatar = user.getEncodedAvatar();
		mimetype = user.getMimeType();

		model.addAttribute("user", username);
		model.addAttribute("avatar", (avatar != null) ? avatar : "");
		model.addAttribute("avatarMimeType", (mimetype != null) ? mimetype : "");
		
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
