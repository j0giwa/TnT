/*
 *  TnT, Todo's 'n' Texts
 *  Copyright (C) 2023  <name of author>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package de.thowl.tnt.core;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import de.thowl.tnt.core.exceptions.DuplicateUserException;
import de.thowl.tnt.core.exceptions.InvalidCredentialsException;
import de.thowl.tnt.core.services.AuthenticationService;
import de.thowl.tnt.storage.GroupRepository;
import de.thowl.tnt.storage.SessionRepository;
import de.thowl.tnt.storage.UserRepository;
import de.thowl.tnt.storage.entities.AccessToken;
import de.thowl.tnt.storage.entities.Session;
import de.thowl.tnt.storage.entities.User;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Implementaion of the {@link AuthenticationService} interface
 * {@inheritDoc}
 */
@Slf4j
@Service
@EnableScheduling
public class AuthenticationServiceImpl implements AuthenticationService {

	private final int BCRYPT_COST = 15;

	@Autowired
	private UserRepository users;

	@Autowired
	private GroupRepository groups;

	@Autowired
	private SessionRepository sessions;

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(BCRYPT_COST);

	/**
	 * Deletes rouge(incative) sessions
	 * 
	 * Runs once every minute
	 */
	@Scheduled(fixedRate = 60000)
	public void cleanupExpiredSessions() {

		log.debug("entering cleanupExpiredSessions");

		Date now;
		List<Session> expired;

		now = new Date();
		expired = sessions.findByExpiresAtBefore(now);

		if (!expired.isEmpty()) {
			log.info("Found {} expired sessions. Deleting...", expired.size());
			sessions.deleteAll(expired);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean validateEmail(String email) {

		log.debug("entering validateEmail");

		String regex;
		boolean result;

		if (null == email)
			return false;

		// Source https://ihateregex.io/expr/email/
		regex = "[^@ \\t\\r\\n]+@[^@ \\t\\r\\n]+\\.[^@ \\t\\r\\n]+";
		
		result = email.matches(regex);

		log.debug("validateEmail(email: {}) returned: {}", email, result);
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean validatePassword(String password) {

		log.debug("entering validatePassword");

		String regex;
		boolean result;

		if (null == password)
			return false;

		// Source = "https://ihateregex.io/expr/password/"
		regex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,}$";

		result = password.matches(regex);

		log.debug("validatePassword(password: {}) returned: {}", password, result);
		return result;
	}

	/**
	 * Sets expiry time (e.g., 30 minutes from now)
	 * 
	 * @param session The session to refresh
	 */
	private void refreshSession(Session session) {
		
		Calendar calendar;
		Date expiryTime;

		calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, 30);
		expiryTime = calendar.getTime();

		session.setExpiresAt(expiryTime);

		this.sessions.save(session);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean validateSession(AccessToken token, String username) {

		log.debug("entering validateSession");

		Session session;
		User user = users.findByUsername(username);
		boolean result;

		if (token == null) {
			log.error("token was null");
			return false;
		}

		session = sessions.findByAuthToken(token.getUsid());
		if (session == null) {
			log.error("a session could not be found");
			return false;
		}

		user = users.findByUsername(username);
		if (user == null) {
			log.error("user: {} could not be found", username);
			return false;
		}

		refreshSession(session);

		result = user.getId() == session.getUserId();
		log.debug("validateSession(token: {}, username:{}) returned: {}", token, username, result);
		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws DuplicateUserException
	 */
	@Override
	public void register(String firstname, String lastname, String username,
			String email, String password) throws DuplicateUserException {

		log.debug("entering register");

		User usr;

		if (this.users.findByEmail(email) != null)
			throw new DuplicateUserException("A User with this Email already exists");

		if (this.users.findByUsername(username) != null)
			throw new DuplicateUserException("A User with this Username already exists");

		usr = new User(firstname, lastname, username, email, encoder.encode(password),
				UUID.randomUUID().toString());

		usr.setGroup(this.groups.findById(1));

		log.info("registering user {} with {}", username, email);
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

		log.debug("entering checkPassword");
	
		String bHash;
		boolean result;

		if (null == password || password.isBlank())
			return false;

		bHash = user.getPassword();

		log.debug("Comparing Form-password with BCrypt-hash");
		result = encoder.matches(password, bHash);

		log.debug("checkPassword(user: {}, password: {}) returned: {}", user.toString(), password, result);

		return result;
	}

	/**
	 * Creates a {@link Session} for the given {@link User}.
	 *
	 * @param user The {@link User} to create a {@link Session} for.
	 * @return The {@link User}s {@link AccessToken}
	 */
	private AccessToken createSession(User user) {

		log.debug("entering createSession");

		AccessToken token;
		UUID uuid;
		Calendar calendar;
		Date expiryTime;

		calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, 30);
		expiryTime = calendar.getTime();

		// Todo: Refactor AccessToken
		token = new AccessToken();
		uuid = UUID.randomUUID();
		token.setUsid(uuid.toString());
		token.setUserId(user.getId());
		token.setLastActive(new Date());

		this.sessions.save(new Session(token.getUsid(), user, expiryTime));

		log.debug("createSession returned: {}", token.toString());

		return token;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AccessToken login(String email, String password) throws InvalidCredentialsException {

		log.debug("entering login");

		User user;

		if (email == null || password == null) {
			log.error("One or more params were left empty");
			throw new InvalidCredentialsException("Params cannot be null");
		}

		user = this.users.findByEmail(email);

		if (user == null) {
			log.error("E-Mail '{}' does not exist", email);
			throw new InvalidCredentialsException("User not found");
		}

		log.info("login attempt for user with email: {}", email);
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

		log.debug("entering logout");

		Session session;
		
		session = this.sessions.findByAuthToken(token);

		log.info("user with id: {} logged out", session.getUserId());
		log.debug("deleting session: {} from Database", session.toString());

		this.sessions.delete(session);
	}

}