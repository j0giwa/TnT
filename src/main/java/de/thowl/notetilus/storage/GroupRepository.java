package de.thowl.notetilus.storage;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;
import de.thowl.notetilus.storage.entities.Group;

@Repository
public interface GroupRepository extends CrudRepository<Group, Long> {
	public Group findById(int id);

	public Group findByName(String name);
}
