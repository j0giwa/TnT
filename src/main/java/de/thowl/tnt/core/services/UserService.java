package de.thowl.tnt.core.services;

import de.thowl.tnt.storage.entities.User;

public interface UserService {
    User getCurrentUser();
    void updateUser(User updateUser);
}
