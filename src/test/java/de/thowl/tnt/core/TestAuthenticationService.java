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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import de.thowl.tnt.core.exceptions.DuplicateUserException;
import de.thowl.tnt.core.exceptions.InvalidCredentialsException;
import de.thowl.tnt.core.services.AuthenticationService;
import de.thowl.tnt.storage.UserRepository;
import de.thowl.tnt.storage.entities.AccessToken;
import de.thowl.tnt.storage.entities.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
class TestAuthenticationService {

	@Autowired
	private AuthenticationService authsvc;

	@Autowired
	private UserRepository users;

	@Test
	void testValidatePassword() {
		log.info("entering test testValidatePassword");
		assertFalse(this.authsvc.validatePassword(null), "'NULL' should not match the requirements");
		assertFalse(this.authsvc.validatePassword("1234"), "'1234' should not match the requirements");
		assertFalse(this.authsvc.validatePassword("lorem"), "'lorm' should not match the requirements");
		assertFalse(this.authsvc.validatePassword("ipsum"), "'ipsum' should not match the requirements");
		assertTrue(this.authsvc.validatePassword("gr3at@3wdsG"), "'gr3at@3wds' should match the requirements");
		assertTrue(this.authsvc.validatePassword("P@ssw0rd"), "'P@ssw0rd' should match the requirements");
		assertFalse(this.authsvc.validatePassword("abcdefghijklmnopqrst"), "should not match the requirements");
	}

	@Test
	void testValidateEmail() {
		log.info("entering test testValidateEmail");
		assertFalse(this.authsvc.validateEmail(null), "'NULL' should not match the requirements");
		assertFalse(this.authsvc.validateEmail("john doe"), "'john doe' should not match the requirements");
		assertFalse(this.authsvc.validateEmail("john doe@test.org"),
				"'john doe@test.org' should not match the requirements");
		assertFalse(this.authsvc.validateEmail("@test.org"), "'@test.org' should not match the requirements");
		assertFalse(this.authsvc.validateEmail("jonh-doe test.org"),
				"'john-doe test.org' should not match the requirements");
		assertTrue(this.authsvc.validateEmail("johndoe@test.org"),
				"'johndoe@test.org' This should match the requirements");
	}

	/**
	 * Testing if a login whith valid credentials returns an accesstoken
	 */
	@Test
	void testLogin() {

		AccessToken token;

		log.info("entering Integration test testLogin");

		token = null;

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

		User usr;
		String firstName;
		String lastName;
		String uname;
		String passwd;
		String email;

		log.info("entering test testRegister");

		firstName = "test";
		lastName = "user";
		uname = "test";
		passwd = "test123";
		email = "test@th-owl.de";

		try {
			this.authsvc.register(firstName, lastName, uname, email, passwd);
		} catch (DuplicateUserException e) {
			fail("this user alredy exists");
		}

		usr = this.users.findByUsername(uname);

		assertNotNull(usr, "User resgistration failed");
		assertEquals(firstName, usr.getFirstname(), "Wrong first name");
		assertEquals(lastName, usr.getLastname(), "Wrong last name");
		assertEquals(uname, usr.getUsername(), "Wrong username");
		assertEquals(email, usr.getEmail(), "Wrong Email");
	}
}
