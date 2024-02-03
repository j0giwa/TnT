package de.thowl.notetilus.storage;

import org.springframework.stereotype.Repository;

import de.thowl.notetilus.storage.entities.Group;

@Repository
public class GroupRepository extends CrudRepository<Group, Long> {
    
}
