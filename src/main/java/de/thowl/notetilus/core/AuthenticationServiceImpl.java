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

    public AccessToken login(String email, String password){
        log.debug("entering login");
        log.info("Login attempt with E-Mail: {}", email);
        AccessToken token = null;

        User usr = this.users.findByEmail(email);

        if (null == usr) {
            // User not registert
            log.error("E-Mail: {} does not exitst", email);
            return null;
        }

        log.info("Comparing Form-password with BCrypt-hash");
        String dbpasswd = usr.getPassword();
        /* log.debug("BCrypt-hash: {}", dbpasswd); */

        if (encoder.matches(password, dbpasswd)) {
            log.info("Password matched, creating user session");
            token = new AccessToken();
            UUID uuid = UUID.randomUUID();
            token.setUSID(uuid.toString());
            token.setUser_id(usr.getId());
            token.setLastactive(new Date());
            this.sessions.save(new Session(token.getUSID(), usr));
        }
            
        return token;
    }
}
