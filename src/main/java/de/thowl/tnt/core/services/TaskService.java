package de.thowl.tnt.core.services;

import java.util.Date;
import java.util.List;

import de.thowl.tnt.storage.entities.Task;

public interface TaskService {

	/**
	 * Adds a new {@link Task} to the Database.
	 *
	 * @param username Name of the {@link User}
	 * @param name     Name (or Title) of the {@link Task}.
	 * @param content  Content of the {@link Task}
	 * @param priority {@link Priority} of the {@link Task}, valid values:
	 *                 {@code low}, {@code medium}, {@code high}
	 * @param dueDate  Date when the {@link Task} is due
	 * @param time     Time when the {@link Task} is due
	 */
	public void add(String username, String name, String content, String priority, Date dueDate, Date time);

	/**
	 * Marks a {@link Task} as done.
	 * 
	 * @param id id of the {@link Task}
	 */
	public void setDone(long id);

	/**
	 * Deletes a {@link Task} from the Database.
	 * 
	 * @param id id of the {@link Task}
	 */
	public void delete(long id);

	/**
	 * Gets all {@link Task} created by a specific {@link User}
	 *
	 * @param username The name of the {@link User}
	 *
	 * @return A list of {@link Task}s,
	 *         {@code null} if the user did not create any tasks,
	 */
	public List<Task> getAll(String username);
}
