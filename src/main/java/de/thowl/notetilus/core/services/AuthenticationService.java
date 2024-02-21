package de.thowl.notetilus.core.services;

import de.thowl.notetilus.core.exeptions.InvalidCredentialsException;
import de.thowl.notetilus.storage.entities.AccessToken;
import de.thowl.notetilus.storage.entities.User;

/**
 * Provides User Authentifaciton/Management funtionalitys
 */
public interface AuthenticationService {

	/**
	 * Performs a login action
	 * 
	 * @param email    The E-Mail address of the user
	 * @param password The password of the user
	 * @return permit A38, or @Code{NULL} when credentials are invalid.
	 */
	public AccessToken login(String email, String password) throws InvalidCredentialsException;

	/**
	 * Registers a new user
	 * 
	 * @param firstname The First Name of the user
	 * @param lastname  The Last Name of the user
	 * @param username  The username of the user
	 * @param email     The E-Mail address of the user
	 * @param password  The password of the user
	 */
	public void register(String firstname, String lastname, String username, String email, String password);

	/**
	 * Validates that the chosen password is somewhat secure.
	 * This is to protect the users from their own stupidity
	 * <p>
	 * Password requirements:
	 * Minimum eight characters,
	 * at least one upper case English letter,
	 * one lower case English letter,
	 * one number and one special character
	 *
	 * @param password Password to validate
	 */
	public boolean validatePassword(String password);

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
	public boolean validateEmail(String email);

	public void logout(String token);

	/**
	 * Validates the Users Session
	 * <p>
	 * Checks if the given ({@link AccessToken} belongs to a session with this
	 * {@link AccessToken}.
	 *
	 * @param token    The {@link AccessToken} to check.
	 * @param username The name of the {@link User} to check.
	 *
	 * @return {@code true} if the conditions match,
	 *         {@code false} if something does not match
	 */
	public boolean validateSession(AccessToken token, String username);
}
