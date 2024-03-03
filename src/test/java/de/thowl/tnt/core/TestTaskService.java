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

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import de.thowl.tnt.core.services.TaskService;
import de.thowl.tnt.storage.TaskRepository;
import de.thowl.tnt.storage.entities.Priority;
import de.thowl.tnt.storage.entities.Task;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
public class TestTaskService {

	@Autowired
	private TaskService tasksvc;

	@Autowired
	private TaskRepository tasks;

	/**
	 * This does inititalise the db this test tasks.
	 * 
	 * 
	 * {@link register} is skipped on purpose, so the tests dont rely on this method
	 * working
	 */
	@BeforeAll
	void initDB() {
		log.info("initialising Testdatabase");

		Task task = new Task();
		task.setId(0);
		task.setName("A Test Task");
		task.setContent("Test task unit");
		task.setPriority(Priority.LOW);
		task.setDueDate(new Date());
		task.setTime(new Date());
		task.setDone(false);
		task.setOverdue(false);

		this.tasks.save(task);
	}

	@Test
	void testSetDone() {
		log.info("entering test testValidatePassword");

		this.tasksvc.setDone(0);

		assertTrue(this.tasks.findById(0).isDone(), "This should be true");
	}

}
