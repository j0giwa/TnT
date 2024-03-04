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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import de.thowl.tnt.core.services.AuthenticationService;
import de.thowl.tnt.core.services.TaskService;
import de.thowl.tnt.storage.entities.AccessToken;
import de.thowl.tnt.web.exceptions.ForbiddenException;
import de.thowl.tnt.web.forms.TaskForm;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class TodoController {

	@Autowired
	private AuthenticationService authsvc;

	@Autowired
	private TaskService tasksvc;

	/**
	 * Shows the todo page
	 * 
	 * @return todo.html
	 */
	@GetMapping("/u/{username}/todo")
	public String showTodoPage(@SessionAttribute(name = "token", required = false) AccessToken token,
			@PathVariable("username") String username, Model model) {
		log.info("entering showLoginPage (GET-Method: /u/{username}/todo)");

		// Prevent unauthrised access
		if (!this.authsvc.validateSession(token, username))
			throw new ForbiddenException("Unathorised access");

		model.addAttribute("user", username);
		model.addAttribute("tasks", this.tasksvc.getAll(username));

		return "todo";
	}

	/**
	 * Adds a new task
	 * 
	 * @return todo.html
	 */
	@PostMapping("/u/{username}/todo")
	public String doAddTask(@SessionAttribute(name = "token", required = false) AccessToken token,
			@PathVariable("username") String username, TaskForm form, Model model,
			HttpSession httpSession) {
		log.info("entering doAddTask (POST-Method: /u/{}/todo)", username);

		// Prevent unauthrised access (in theory redundant, but i keep this anyway)
		if (!this.authsvc.validateSession(token, username))
			throw new ForbiddenException("Unathorised access");

		this.tasksvc.add(username, form.getTaskName(), form.getTaskContent(), form.getPriority(),
				form.getDate(), form.getTime());

		return "redirect:/u/" + username + "/todo";
	}

	/**
	 * Marks a task as done
	 * 
	 * @return todo.html
	 */
	@PostMapping("/u/{username}/todo/done")
	public String doMarkAsDone(@SessionAttribute(name = "token", required = false) AccessToken token,
			@PathVariable("username") String username, TaskForm form, Model model,
			HttpSession httpSession) {
		log.info("entering doAddTask (POST-Method: /u/{}/todo/done)", username);

		// Prevent unauthrised access (in theory redundant, but i keep this anyway)
		if (!this.authsvc.validateSession(token, username))
			throw new ForbiddenException("Unathorised access");

		this.tasksvc.setDone(form.getId());

		return "redirect:/u/" + username + "/todo";
	}

	/**
	 * Deletes a task
	 * 
	 * @return todo.html
	 */
	@DeleteMapping("/u/{username}/todo")
	public String doDeleteTask(@SessionAttribute(name = "token", required = false) AccessToken token,
			@PathVariable("username") String username, TaskForm form, Model model,
			HttpSession httpSession) {
		log.info("entering doDeleteTask (DELETE-Method: /u/{}/todo)", username);

		// Prevent unauthrised access (in theory redundant, but i keep this anyway)
		if (!this.authsvc.validateSession(token, username))
			throw new ForbiddenException("Unathorised access");

		this.tasksvc.delete(form.getId());

		return "redirect:/u/" + username + "/todo";
	}

}
