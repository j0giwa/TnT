package de.thowl.notetilus.storage;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.thowl.notetilus.storage.entities.AccessToken;
import de.thowl.notetilus.storage.entities.Session;

@Repository
public interface SessionRepository extends CrudRepository<Session, Long> {
	public Session findById(long id);
	public Session findByAuthToken(String authToken);
	public Session findByUserId(long userId);
}
