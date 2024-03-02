package de.thowl.tnt.core.services;

import java.util.Date;
import java.util.List;

import de.thowl.tnt.storage.entities.Task;

public interface TaskService {

	/**
	 * Adds a new {@link Task} to the Database.
	 *
	 * @param username
	 *
	 */
	public void add(String username, String name, String content, String priority, Date dueDate, Date time);

	public List<Task> getAll(String username);

	public void delete(long id);

}
