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

package de.thowl.tnt.core.services;

import de.thowl.tnt.core.exeptions.InvalidCredentialsException;
import de.thowl.tnt.storage.entities.AccessToken;
import de.thowl.tnt.storage.entities.User;

/**
 * Provides User Authentifaciton/Management funtionalitys
 */
public interface AuthenticationService {

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
	 * Performs a login action and stores an active {@link Session} in the Database.
	 * 
	 * @param email    The E-Mail address of the user
	 * @param password The password of the user
	 * @return permit A38, or @Code{NULL} when credentials are invalid.
	 */
	public AccessToken login(String email, String password) throws InvalidCredentialsException;

	/**
	 * Removes an active {@link Session} from the Database.
	 * 
	 * @param token The token of the session to remove.
	 */
	public void logout(String token);

}