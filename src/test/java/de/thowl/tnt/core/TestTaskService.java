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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Date;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import de.thowl.tnt.core.services.TaskService;
import de.thowl.tnt.storage.TaskRepository;
import de.thowl.tnt.storage.UserRepository;
import de.thowl.tnt.storage.entities.User;
import de.thowl.tnt.storage.entities.Task;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
public class TestTaskService {

	@Autowired
	private TaskService tasksvc;

	@Autowired
	private TaskRepository tasks;

	@Autowired
	private UserRepository users;

	@Test
	void testToggleDone() {
		log.info("entering test testToggleDone");

		this.tasksvc.toggleDone(1);
		assertTrue(this.tasks.findById(1).isDone(), "This should be true");

		this.tasksvc.toggleDone(1);
		assertFalse(this.tasks.findById(1).isDone(), "This should be false");
	}

	@Test
	public void testAddTask() {
		log.info("entering test testAddTask");

		long taskAmount;

		taskAmount = this.tasks.count();
		this.tasksvc.add("Tasktester", "Test Task", "Task Content", "High", new Date(), new Date());
		assertEquals(taskAmount + 1, this.tasks.count());
	}

	@Test
	public void testDeleteTask() {
		log.info("entering test testDeleteTask");

		long taskAmount;

		taskAmount = this.tasks.count();
		this.tasksvc.delete(1);
		assertEquals(taskAmount - 1, this.tasks.count());
	}

	@Test
	public void testGetAllTasks() {
		log.info("entering test testGetAllTasks");

		User user;
		List<Task> tasks;
		List<Task> result;

		user = this.users.findByUsername("Tasktester");
		tasks = this.tasks.findByUser(user);
		result = this.tasksvc.getAllTasks("Tasktester");

		assertEquals(tasks.size(), result.size(), "These should be the same size");
	}

}