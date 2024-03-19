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
package de.thowl.tnt.storage;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import de.thowl.tnt.storage.entities.Task;
import de.thowl.tnt.storage.entities.User;
import de.thowl.tnt.storage.entities.Priority;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {

	public Task findById(long id);

	public List<Task> findByUser(User User);

	public List<Task> findByDone(boolean done);

	public List<Task> findByUserAndOverdue(User User, boolean overdue);

	public List<Task> findByUserAndPriority(User User, Priority priority);

	@Query("SELECT t FROM Task t WHERE CONCAT(t.dueDate, ' ', t.time) < :currentDateTime")
	public List<Task> findByDueDateTimeBefore(@Param("currentDateTime") String currentDateTime);
}
