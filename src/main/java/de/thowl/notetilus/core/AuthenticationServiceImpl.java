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


/**
 * Ipmlementaion of the {@link AuthenticationService} interface
* {@inheritDoc}
*/
@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
	
	@Autowired
	private UserRepository users;
	@Autowired
	private SessionRepository sessions;

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(15);
	
	/**
	 * Checks if the input password matches the {@link User}s password stored in the Database.
	 *
	 * @param user {@link User} whose Password should be checked against
	 * @param password The input Password that should be checked
	 *
	 * @return {@code true} if the Password matched, {@code false} if the Password did not match
	 */
	private boolean checkPassword(User user, String password) {
		log.info("Comparing Form-password with BCrypt-hash");
		String dbPassword = user.getPassword();
		return encoder.matches(password, dbPassword);
	}
	
	/**
	 * Creates a {@link Session} for the given {@link User}.
	 *
	 * @param user The {@link User} to create a {@link Session} for.
	 * @return The {@link User}s {@link AccessToken}
	 */
	private AccessToken createSession(User user) {
		AccessToken token = new AccessToken();
		UUID uuid = UUID.randomUUID();
		token.setUSID(uuid.toString());
		token.setUserId(user.getId());
		token.setLastActive(new Date());
		this.sessions.save(new Session(token.getUSID(), user));
		return token;
	}

	/**
	 * {@inheritDoc}
	 */
	public AccessToken login(String email, String password){
		log.debug("entering login");
		
		User user = this.users.findByEmail(email);
		if (user == null) {
			log.error("User with E-Mail '{}' does not exist", email);
			return null;
		}
	
		if (checkPassword(user, password)) {
			log.info("Password matched, creating user session");
			return createSession(user);
		}
		
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void register(String username, String email, String password, String password2){
		// TODO: Password MUST be BCrypt-encrypted
	}
}
