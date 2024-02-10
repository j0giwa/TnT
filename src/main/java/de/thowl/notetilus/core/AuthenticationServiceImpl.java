package de.thowl.notetilus.core;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.thowl.notetilus.core.services.AuthenticationService;
import de.thowl.notetilus.storage.SessionRepository;
import de.thowl.notetilus.storage.UserRepository;
import de.thowl.notetilus.storage.entities.AccessToken;
import de.thowl.notetilus.storage.entities.Session;
import de.thowl.notetilus.storage.entities.User;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    
    @Autowired
    private UserRepository users;
    @Autowired
    private SessionRepository sessions;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(15);
    
    private boolean isPasswordCorrect(User user, String password) {
        log.info("Comparing Form-password with BCrypt-hash");
        String dbPassword = user.getPassword();
        return encoder.matches(password, dbPassword);
    }
    
    private AccessToken createSession(User user) {
        AccessToken token = new AccessToken();
        UUID uuid = UUID.randomUUID();
        token.setUSID(uuid.toString());
        token.setUserId(user.getId());
        token.setLastActive(new Date());
        this.sessions.save(new Session(token.getUSID(), user));
        return token;
    }

    public AccessToken login(String email, String password){
        log.debug("entering login");
        
        User user = this.users.findByEmail(email);
        if (user == null) {
            log.error("User with E-Mail '{}' does not exist", email);
            return null;
        }
    
        if (isPasswordCorrect(user, password)) {
            log.info("Password matched, creating user session");
            return createSession(user);
        }
        
        return null;
    }
    

    public void register(String username, String email, String password, String password2){
        // TODO: Password MUST be BCrypt-encrypted
    }
}
