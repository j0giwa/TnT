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

package de.thowl.tnt.core.services;

import java.util.Date;
import java.util.List;

import de.thowl.tnt.storage.entities.Priority;
import de.thowl.tnt.storage.entities.Task;
import de.thowl.tnt.storage.entities.User;

public interface TaskService {

	/**
	 * Adds a new {@link Task} to the Database.
	 *
	 * @param userId   Name of the {@link User} creating the {@link Task}.
	 * @param name     Name (or Title) of the {@link Task}.
	 * @param content  Content of the {@link Task}
	 * @param priority {@link Priority} of the {@link Task}, valid values:
	 *                 {@code low}, {@code medium}, {@code high}
	 * @param dueDate  Date when the {@link Task} is due
	 * @param time     Time when the {@link Task} is due
	 */
	public void add(long userId, String name, String content, String priority, Date dueDate, Date time);

	/**
	 * Marks a {@link Task} as done.
	 * 
	 * @param id     id of the {@link Task}.
	 * @param userId The ID of the {@link User} to verify ownership.
	 */
	public void toggleDone(long id, long userId);

	/**
	 * Deletes a {@link Task} from the Database.
	 * 
	 * @param id     id of the {@link Task}
	 * @param userId The ID of the {@link User} to verify ownership
	 */
	public void delete(long id, long userId);

	/**
	 * Gets all {@link Task} created by a specific {@link User}
	 *
	 * @param userId The ID of the {@link User}.
	 *
	 * @return A list of {@link Task}s,
	 *         {@code null} if the user did not create any tasks,
	 */
	public List<Task> getAllTasks(long userId);

	/**
	 * Gets all overdue {@link Task} created by a specific {@link User}
	 *
	 * @param userId The ID of the {@link User}.
	 *
	 * @return A list of overdue {@link Task}s,
	 *         {@code null} if the user did not create any tasks,
	 */
	public List<Task> getAllOverdueTasks(long userId);

	/**
	 * Gets all {@link Task}s with the given created {@link Priority}
	 *
	 * @param userId The ID of the {@link User}.
	 * @param priority The {@link Priority} of the {@link Task}s.
	 *
	 * @return A list of {@link Task}s,
	 *         {@code null} if the user did not create any tasks,
	 */
	public List<Task> getTasksByPriority(long userId, Priority priority);

}
