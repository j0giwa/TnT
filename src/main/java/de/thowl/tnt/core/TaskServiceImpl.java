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

package de.thowl.tnt.core;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.thowl.tnt.core.services.TaskService;
import de.thowl.tnt.storage.TaskRepository;
import de.thowl.tnt.storage.UserRepository;
import de.thowl.tnt.storage.entities.Priority;
import de.thowl.tnt.storage.entities.Task;
import de.thowl.tnt.storage.entities.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TaskServiceImpl implements TaskService {

	@Autowired
	private UserRepository users;

	@Autowired
	private TaskRepository tasks;

	public Priority setPriority(String priority) {
		switch (priority) {
			case "low":
				return Priority.LOW;
			case "medium":
				return Priority.MEDIUM;
			case "high":
				return Priority.HIGH;
			default:
				return Priority.LOW; // Assume it was low priority
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(String username, String name, String content, String priority, Date dueDate, Date time) {
		log.debug("entering add");

		User user = users.findByUsername(username);
		Task task = Task.builder().user(user).name(name).content(content).createdAt(new Date())
				.dueDate(dueDate).time(time).priority(setPriority(priority))
				.overdue(false).done(false).build();

		this.tasks.save(task);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDone(long id) {
		log.debug("entering add");

		Task task = this.tasks.findById(id);	


		log.info("marking task id: {} as done", id);
		task.setDone(true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(long id) {
		log.debug("entering delete");

		Task task = this.tasks.findById(id);	
		log.info("deleting task id: {}", id);
		this.tasks.delete(task);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Task> getAll(String username) {
		log.debug("entering getAll");

		User user = users.findByUsername(username);

		return this.tasks.findByUser(user);
	}

}
