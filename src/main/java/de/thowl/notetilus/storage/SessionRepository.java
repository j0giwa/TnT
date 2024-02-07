package de.thowl.notetilus.storage;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.thowl.notetilus.storage.entities.Session;

@Repository
public interface SessionRepository extends CrudRepository<Session, Long> {
    public Session findById(int id);
    public Session findByAuthToken(String authToken);
}
