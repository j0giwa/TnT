
package de.thowl.tnt.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import de.thowl.tnt.core.services.AuthenticationService;
import de.thowl.tnt.core.services.UserService;
import de.thowl.tnt.storage.UserRepository;
import de.thowl.tnt.storage.entities.User;
import de.thowl.tnt.web.forms.RegisterForm;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationService authenticationService;
   
    
    private RegisterForm userForm;
   
    @Override
    public User getCurrentUser() {
        userForm = new RegisterForm();
        //authenticationService = (AuthenticationService) SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = userForm.getUsername();
        User currentUser = userRepository.findByUsername(currentUsername);
       
        return currentUser;
        
    }

    @Override
    public void updateUser (User updUser) {

    }
}
