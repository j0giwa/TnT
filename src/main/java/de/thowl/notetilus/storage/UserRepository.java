package de.thowl.notetilus.storage;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.thowl.notetilus.storage.entities.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
	public User findByUsername(String username);
	public User findByEmail(String email);
}
