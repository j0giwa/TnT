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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import de.thowl.tnt.core.services.TaskService;
import de.thowl.tnt.storage.TaskRepository;
import de.thowl.tnt.storage.UserRepository;
import de.thowl.tnt.storage.entities.Priority;
import de.thowl.tnt.storage.entities.Task;
import de.thowl.tnt.storage.entities.User;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementaion of the {@link TaskService} interface
 * {@inheritDoc}
 */
@Slf4j
@Service
@EnableScheduling
public class TaskServiceImpl implements TaskService {

	@Autowired
	private UserRepository users;

	@Autowired
	private TaskRepository tasks;

	/**
	 * Marks all overdue task as such
	 * 
	 * Runs once every minute
	 */
	@Scheduled(fixedRate = 60000)
	public void flagTasksAsOverdue() {

		DateTimeFormatter formatter;
		String now;
		List<Task> tasks;

		log.debug("entering flagTasksAsOverdue");

		formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		now = LocalDateTime.now().format(formatter);

		tasks = this.tasks.findByDueDateTimeBefore(now);

		if (!tasks.isEmpty()) {
			log.info("Found {} overdue tasks.", tasks.size());
			for (Task task : tasks) {
				task.setOverdue(true);
			}
		}
	}

	/**
	 * Deletes done tasks
	 * 
	 * Runs once every minute
	 */
	@Scheduled(fixedRate = 60000)
	public void cleanupDoneTasks() {

		List<Task> doneTasks;

		log.debug("entering cleanupDoneTasks");

		doneTasks = this.tasks.findByDone(true);

		if (!doneTasks.isEmpty()) {
			log.info("Deleted {} done tasks", doneTasks.size());
			this.tasks.deleteAll(doneTasks);
		}
	}

	public Priority setPriority(String priority) {
		switch (priority.toLowerCase()) {
			default:
			case "low":
				return Priority.LOW;
			case "medium":
				return Priority.MEDIUM;
			case "high":
				return Priority.HIGH;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(long userId, String name, String content,
			String priority, Date dueDate, Date time) {

		User user;
		Task task;

		log.debug("entering add");

		user = users.findById(userId).get();
		task = Task.builder()
				.user(user)
				.name(name)
				.content(content)
				.createdAt(new Date())
				.dueDate(dueDate)
				.time(time)
				.priority(setPriority(priority))
				.overdue(false)
				.done(false)
				.build();

		if (null != task)
			this.tasks.save(task);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void toggleDone(long id, long userId) {

		User user;
		Task task;

		log.debug("entering add");

		user = users.findById(userId).get();
		task = this.tasks.findById(id);

		if (null != task) {
			if (user.getId() == task.getUser().getId()) {
				log.info("switching state of task id: {}", id);
				task.setDone(!task.isDone());

				this.tasks.save(task);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(long id, long userId) {

		User user;
		Task task;

		log.debug("entering delete");

		user = users.findById(userId).get();
		task = this.tasks.findById(id);

		if (null != task) {

			if (user.getId() == task.getUser().getId()) {
				log.info("deleting task id: {}", id);
				this.tasks.delete(task);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Task> getAllTasks(long userId) {

		User user;

		log.debug("entering getAllTasks");

		user = users.findById(userId).get();

		return this.tasks.findByUser(user);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Task> getAllOverdueTasks(long userId) {

		User user;

		log.debug("entering getAllOverdueTasks");

		user = users.findById(userId).get();

		return this.tasks.findByUserAndOverdue(user, true);
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Task> getTasksByPriority(long userId, Priority priority) {

		User user;

		log.debug("entering getTasksByPriority");

		user = users.findById(userId).get();

		return this.tasks.findByUserAndPriority(user, priority);
	}
}
