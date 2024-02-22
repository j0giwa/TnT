package de.thowl.notetilus.core;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.thowl.notetilus.core.exeptions.InvalidCredentialsException;
import de.thowl.notetilus.core.services.AuthenticationService;
import de.thowl.notetilus.storage.GroupRepository;
import de.thowl.notetilus.storage.SessionRepository;
import de.thowl.notetilus.storage.UserRepository;
import de.thowl.notetilus.storage.entities.AccessToken;
import de.thowl.notetilus.storage.entities.Session;
import de.thowl.notetilus.storage.entities.User;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Implementaion of the {@link AuthenticationService} interface
 * {@inheritDoc}
 */
@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	private final int BCRYPT_COST = 15;

	@Autowired
	private UserRepository users;
	@Autowired
	private GroupRepository groups;
	@Autowired
	private SessionRepository sessions;

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(BCRYPT_COST);

	public boolean validateEmail(String email) {
		log.debug("entering validateEmail(email: {})", email);

		if (null == email)
			return false;

		// Source https://ihateregex.io/expr/email/
		return email.matches("[^@ \\t\\r\\n]+@[^@ \\t\\r\\n]+\\.[^@ \\t\\r\\n]+");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean validatePassword(String password) {
		log.debug("entering validatePassword(password: {})", password);

		if (null == password)
			return false;

		// Source = "https://ihateregex.io/expr/password/"
		return password.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,}$");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean validateSession(AccessToken token, String username) {
		log.debug("entering validateSession(token: {}, username: {})", token.toString(), username);

		Session session = this.sessions.findByAuthToken(token.getUsid());
		User user = this.users.findByUsername(username);

		return user.getId() == session.getUserId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void register(String firstname, String lastname, String username, String email, String password) {
		log.debug("entering register(firstname: {}, lastname: {}, username: {}, email: {}, password: {}",
				firstname, lastname, username, email, password);

		User usr = new User();
		usr.setFirstname(firstname);
		usr.setLastname(lastname);
		usr.setUsername(username);
		usr.setEmail(email);
		usr.setPassword(encoder.encode(password));
		usr.setGroup(this.groups.findById(1));

		this.users.save(usr);
	}

	/**
	 * Checks if the input password matches the {@link User}s password stored in the
	 * Database.
	 *
	 * @param user     The {@link User} whose Password should be checked against
	 * @param password The input Password that should be checked
	 *
	 * @return {@code true} if the Password matched, {@code false} if the Password
	 *         did not match
	 */
	private boolean checkPassword(User user, String password) {
		log.debug("entering checkPassword(user: {}, password: {})", user.toString(), password);

		if (null == password || password.isBlank())
			return false;

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
		log.debug("entering createSession(user: {})", user.toString());

		AccessToken token = new AccessToken();
		UUID uuid = UUID.randomUUID();
		token.setUsid(uuid.toString());
		token.setUserId(user.getId());
		token.setLastActive(new Date());
		this.sessions.save(new Session(token.getUsid(), user));

		return token;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AccessToken login(String email, String password) throws InvalidCredentialsException {
		log.debug("entering login(email: {} password: {})", email, password);

		if (email == null || password == null) {
			log.error("One or more params were left empty");
			throw new InvalidCredentialsException("Params cannot be null");
		}

		User user = this.users.findByEmail(email);

		if (user == null) {
			log.error("E-Mail '{}' does not exist", email);
			throw new InvalidCredentialsException("User not found");
		}

		if (checkPassword(user, password)) {
			log.info("Password matched, creating user session");
			return createSession(user);
		}

		throw new InvalidCredentialsException("Wrong Password");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void logout(String token) {
		log.debug("entering logout(token: {})", token);

		Session session = this.sessions.findByAuthToken(token);
		this.sessions.delete(session);
	}

}
