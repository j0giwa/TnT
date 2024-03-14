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

package de.thowl.tnt.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.thowl.tnt.core.services.TaskService;
import de.thowl.tnt.storage.TaskRepository;
import de.thowl.tnt.storage.UserRepository;
import de.thowl.tnt.storage.entities.User;
import de.thowl.tnt.web.api.schema.TaskSchema;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/task")
@Tag(name = "task", description = "task API")
public class TaskApi {

	@Autowired
	private UserRepository users;

	@Autowired
	private TaskRepository tasks;

	@Autowired
	private TaskService tasksvc;

	/**
	 * @param apiToken The apitoken of the user.
	 * @param id       The id of the task.
	 * 
	 * @return {@code 200} when the task was marked as done,
	 *         {@code 403} when the requesting user fails to authenticate
	 */
	@Operation(summary = "Retrieves a task from the Database")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Task as json", content = @Content),
			@ApiResponse(responseCode = "403", description = "Unauthorised api token", content = @Content)
	})
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public ResponseEntity<Object> getTask(@Parameter(description = "Your api token") @RequestParam String apiToken,
			@Parameter(description = "The id of the task") @RequestParam long id) {
		log.info("entering getTask (GET-Method: /api/task/get)");

		if (users.findByApiToken(apiToken) == null)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unathorised");

		return ResponseEntity.status(HttpStatus.OK).body(this.tasks.findById(id));
	}

	/**
	 * Adds a new task
	 * 
	 * @param apiToken The apitoken of the user
	 * @param task     The task to add.
	 * 
	 * @return {@code 200} when the task was added to the database,
	 *         {@code 403} when the requesting user fails to authenticate
	 */
	@Operation(summary = "Adds a new task to the database")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Success", content = @Content),
			@ApiResponse(responseCode = "403", description = "Unauthorised or missing token", content = @Content)
	})
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ResponseEntity<String> addTask(@Parameter(description = "Your api token") @RequestParam String apiToken,
			@RequestBody TaskSchema task) {
		log.info("entering addTask (POST-Method: /api/task/add)");

		String username;
		User user;

		user = users.findByApiToken(apiToken);

		if (user == null)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unathorised");

		username = user.getUsername();

		this.tasksvc.add(username, task.getTaskName(), task.getTaskContent(), task.getPriority(),
				task.getDate(), task.getTime());

		return ResponseEntity.status(HttpStatus.OK).body("success");
	}

	/**
	 * Switches the status of a task
	 * 
	 * @param apiToken The apitoken of the user.
	 * @param id       The id of the task.
	 * 
	 * @return {@code 200} when the task was marked as done,
	 *         {@code 403} when the requesting user fails to authenticate
	 */
	@Operation(summary = "Switches the status of a task")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "success", content = @Content),
			@ApiResponse(responseCode = "403", description = "Unauthorised", content = @Content)
	})
	@RequestMapping(value = "/done", method = RequestMethod.PATCH)
	public ResponseEntity<String> markTaskDone(
			@Parameter(description = "Your api token") @RequestParam String apiToken,
			@Parameter(description = "The id of the task") @RequestParam long id) {
		log.info("entering markTaskDone (POST-Method: /api/task/done)");

		String username;
		User user;

		user = users.findByApiToken(apiToken);

		if (user == null)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unathorised");

		username = user.getUsername();

		this.tasksvc.toggleDone(id, username);

		return ResponseEntity.status(HttpStatus.OK).body("success");
	}

	/**
	 * Deletes a task
	 * 
	 * @param apiToken The apitoken of the user.
	 * @param id       The id of the task.
	 * 
	 * @return {@code 200} when the task was deleted,
	 *         {@code 403} when the requesting user fails to authenticate
	 */
	@Operation(summary = "Deletes a task from the database")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "success", content = @Content),
			@ApiResponse(responseCode = "403", description = "Unauthorised", content = @Content)
	})
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteTask(
			@Parameter(description = "Your api token") @RequestParam String apiToken,
			@Parameter(description = "The id of the task") @RequestParam long id) {
		log.info("entering deleteTask (DELETE-Method: /api/task/delete)");

		String username;
		User user;

		user = users.findByApiToken(apiToken);

		if (user == null)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unathorised");

		username = user.getUsername();

		this.tasksvc.delete(id, username);

		return ResponseEntity.status(HttpStatus.OK).body("success");
	}

}
