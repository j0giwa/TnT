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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;

import de.thowl.tnt.core.services.AuthenticationService;
import de.thowl.tnt.core.services.TaskService;
import de.thowl.tnt.storage.entities.AccessToken;
import de.thowl.tnt.storage.entities.User;
import de.thowl.tnt.web.forms.TaskForm;
import jakarta.servlet.http.HttpServletRequest;
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
	@RequestMapping(value = "/u/{username}/todo", method = RequestMethod.GET)
	public String showTodoPage(@SessionAttribute(name = "token", required = false) AccessToken token,
			@PathVariable("username") String username, Model model) {

		long userId;
		User user;
		String avatar, mimetype;

		log.info("entering showLoginPage (GET-Method: /u/{}/todo)", username);

		// Prevent unauthrised access
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

		model.addAttribute("tasks", this.tasksvc.getAllTasks(userId));

		return "todo";
	}

	/**
	 * Adds a new task
	 * 
	 * @return todo.html
	 */
	@RequestMapping(value = "/u/{username}/todo", method = RequestMethod.POST)
	public String doAddTask(HttpServletRequest request,
			@SessionAttribute(name = "token", required = false) AccessToken token,
			@PathVariable("username") String username, TaskForm form, Model model) {

		long userId;
		User user;
		String referer;

		log.info("entering doAddTask (POST-Method: /u/{}/todo)", username);

		// Prevent unauthrised access
		if (!this.authsvc.validateSession(token, username))
			throw new AccessDeniedException("Access Forbidden");

		this.authsvc.refreshSession(token);

		user = this.authsvc.getUserbySession(token);
		userId = user.getId();

		try {
			this.tasksvc.add(userId, form.getTaskName(), form.getTaskContent(), form.getPriority(),
					form.getDate(), form.getTime());
		} catch (NullPointerException e) {
			model.addAttribute("error", "input_error");
		}

		referer = request.getHeader("Referer");
		return "redirect:" + referer;
	}

	/**
	 * Marks a task as done
	 * 
	 * @return todo.html
	 */
	@RequestMapping(value = "/u/{username}/todo/done", method = RequestMethod.POST)
	public String doMarkAsDone(HttpServletRequest request,
			@SessionAttribute(name = "token", required = false) AccessToken token,
			@PathVariable("username") String username, TaskForm form, Model model) {

		long userId;
		User user;
		String referer;

		log.info("entering doAddTask (POST-Method: /u/{}/todo/done)", username);

		// Prevent unauthrised access
		if (!this.authsvc.validateSession(token, username))
			throw new AccessDeniedException("Access Forbidden");

		this.authsvc.refreshSession(token);

		user = this.authsvc.getUserbySession(token);
		userId = user.getId();

		this.tasksvc.toggleDone(form.getId(), userId);

		referer = request.getHeader("Referer");
		return "redirect:" + referer;
	}

	/**
	 * Deletes a task
	 * 
	 * @return todo.html
	 */
	@RequestMapping(value = "/u/{username}/todo", method = RequestMethod.DELETE)
	public String doDeleteTask(HttpServletRequest request,
			@SessionAttribute(name = "token", required = false) AccessToken token,
			@PathVariable("username") String username, TaskForm form, Model model) {

		long userId;
		User user;
		String referer;

		log.info("entering doDeleteTask (DELETE-Method: /u/{}/todo)", username);

		// Prevent unauthrised access
		if (!this.authsvc.validateSession(token, username))
			throw new AccessDeniedException("Access Forbidden");

		this.authsvc.refreshSession(token);

		user = this.authsvc.getUserbySession(token);
		userId = user.getId();

		this.tasksvc.delete(form.getId(), userId);

		referer = request.getHeader("Referer");
		return "redirect:" + referer;
	}

}
