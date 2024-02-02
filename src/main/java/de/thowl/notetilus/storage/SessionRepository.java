package de.thowl.notetilus.storage;

import org.springframework.data.repository.CrudRepository;

import de.thowl.notetilus.storage.entities.Session;

public interface SessionRepository extends CrudRepository<Session, Long> {

}
