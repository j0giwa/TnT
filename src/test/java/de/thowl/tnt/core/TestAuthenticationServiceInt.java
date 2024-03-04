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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import de.thowl.tnt.core.exeptions.DuplicateUserException;
import de.thowl.tnt.core.exeptions.InvalidCredentialsException;
import de.thowl.tnt.core.services.AuthenticationService;
import de.thowl.tnt.storage.UserRepository;
import de.thowl.tnt.storage.entities.AccessToken;
import de.thowl.tnt.storage.entities.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
class TestAuthenticationServiceInt {

	@Autowired
	private AuthenticationService authsvc;

	@Autowired
	private UserRepository users;

	/**
	 * Testing if a login whith valid credentials returns an accesstoken
	 */
	@Test
	void testLogin() {
		log.info("entering Integration test testLogin");
		AccessToken token = null;

		try {
			token = this.authsvc.login("ruediger@thowl.de", "P@ssw0rd");
		} catch (InvalidCredentialsException e) {
			fail("login with valid credentials should not throw an exception");
		}

		assertNotNull(token, "Token should not be NULL");
		assertNotNull(token.getUsid(), "Session ID should not be NULL");
		assertEquals(1, token.getUserId(), "Wrong UserID in session token, expected = 1");
	}

	/**
	 * Testing if a login whith invalid credentials returns nothing
	 */
	@Test
	void testLoginInvalidCredetials() {
		log.info("entering Integration test testLoginInvalidCredetials, there should be errors");

		log.info("Attempting to login without credentials");
		assertThrows(InvalidCredentialsException.class, () -> {
			this.authsvc.login(null, null);
		}, "Token should be NULL, no credentials");

		log.info("Attempting to login without an email");
		assertThrows(InvalidCredentialsException.class, () -> {
			this.authsvc.login(null, "BringMe115");
		}, "Token should be NULL, one or more inputs were NULL");

		log.info("Attempting to login without an password");
		assertThrows(InvalidCredentialsException.class, () -> {
			this.authsvc.login("ruediger@thowl.de", null);
		}, "Token should be NULL, one or more inputs were NULL");

		log.info("Attempting to login with a wrong password");
		assertThrows(InvalidCredentialsException.class, () -> {
			this.authsvc.login("ruediger@thowl.de", "BringMe115");
		}, "Token should be NULL, wrong password");

		log.info("Attempting to login with a user that does not exist");
		assertThrows(InvalidCredentialsException.class, () -> {
			// NOTE: password does not matter
			this.authsvc.login("ivar@test.de", "1234");
		}, "Token should be NULL, user does not exist");
	}

	@Test
	void testRegister() {
		log.info("entering test testRegister");

		String firstName = "test";
		String lastName = "user";
		String uname = "test";
		String passwd = "test123";
		String email = "test@th-owl.de";

		try {
			this.authsvc.register(firstName, lastName, uname, email, passwd);
		} catch (DuplicateUserException e) {
			fail("this user alredy exists");
		}

		User usr = this.users.findByUsername(uname);

		assertNotNull(usr, "User resgistration failed");
		assertEquals(firstName, usr.getFirstname(), "Wrong first name");
		assertEquals(lastName, usr.getLastname(), "Wrong last name");
		assertEquals(uname, usr.getUsername(), "Wrong username");
		assertEquals(email, usr.getEmail(), "Wrong Email");
	}

}
