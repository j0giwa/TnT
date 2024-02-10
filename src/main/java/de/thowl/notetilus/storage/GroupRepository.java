package de.thowl.notetilus.storage;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;
import de.thowl.notetilus.storage.entities.Group;
import de.thowl.notetilus.storage.entities.Session;

@Repository
public interface GroupRepository extends CrudRepository<Group, Long> {
	public Session findById(int id);
	public Session findByName(String name);
}
