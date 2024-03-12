
package de.thowl.tnt.core;

import org.springframework.stereotype.Service;

import de.thowl.tnt.core.services.UserService;
import de.thowl.tnt.storage.entities.User;

@Service
public class UserServiceImpl implements UserService {

    public User getCurrentUser() {
        return null;
        
    }

    public void updateUser (User updUser) {

    }
}
