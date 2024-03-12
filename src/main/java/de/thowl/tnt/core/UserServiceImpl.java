
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
   
    @Autowired
    private User userForm;
   
    @Override
    public User getCurrentUser() {
        //authenticationService = (AuthenticationService) SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepository.findByUsername(userForm.getUsername());
       
        return currentUser;
        
    }

    /*
     * Retrieve and update the User-Obeject from the database
     */
    @Override
    public void updateUser (User updUser) {

        //update the userobject in database
        User existingUser = userRepository.findById(updUser.getId()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        existingUser.setUsername(updUser.getUsername());
        existingUser.setEmail(updUser.getEmail());
        existingUser.setFirstname(updUser.getFirstname());
        existingUser.setLastname(updUser.getLastname());

        userRepository.save(existingUser); //Save the changes in the database
    }
}
