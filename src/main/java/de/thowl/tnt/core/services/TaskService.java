package de.thowl.tnt.core.services;

import java.util.Date;
import java.util.List;

import de.thowl.tnt.storage.entities.Priority;
import de.thowl.tnt.storage.entities.Task;

public interface TaskService {

	public void add(String username, String name, String content, Priority priority, Date date);

	public List<Task> getAll(String username);

	public void delete();

}
