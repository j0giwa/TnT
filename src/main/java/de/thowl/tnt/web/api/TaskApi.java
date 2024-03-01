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

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import de.thowl.tnt.core.services.TaskService;
import de.thowl.tnt.storage.entities.Priority;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/task")
@Tag(name = "teapot", description = "teapot API")
public class TaskApi {

	@Autowired
	private TaskService tasksvc;

	@Operation(summary = "Adds a task to the database")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "success", content = @Content),
			@ApiResponse(responseCode = "403", description = "I'm a teapot", content = @Content)
	})

	@PostMapping("/add")
	public ResponseEntity<String> add(@RequestBody String username, @RequestBody String taskName,
			@RequestBody String taskContent, @RequestBody String taskPriority, @RequestBody Date date) {

		Priority priority = null;
		switch (taskPriority) {
			case "low":
				priority = Priority.LOW;
				break;
			case "medium":
				priority = Priority.MEDIUM;
				break;
			case "high":
				priority = Priority.HIGH;
				break;
			default:
				break;
		}

		this.tasksvc.add(username, taskName, taskContent, priority, date);

		return ResponseEntity.status(HttpStatus.OK).body("success");
	}

}
