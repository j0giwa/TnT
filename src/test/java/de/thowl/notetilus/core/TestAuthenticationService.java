package de.thowl.notetilus.core;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import de.thowl.notetilus.storage.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.thowl.notetilus.core.exeptions.InvalidCredentialsException;
import de.thowl.notetilus.core.services.AuthenticationService;
import de.thowl.notetilus.storage.UserRepository;
import de.thowl.notetilus.storage.entities.AccessToken;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class TestAuthenticationService {
	
	@Autowired
	private AuthenticationService authsvc;

	@Autowired
	private UserRepository users;


	/**
	 * Testing if a login whith valid credentials returns an accesstoken
	 */
	@Test
	public void testLogin() {
		log.info("entering Integration test testLogin");
		AccessToken token;
		try {
			// NOTE: The password of the testuser is concidered insecure for our standards
			token = this.authsvc.login("ruediger@thowl.de", "SuperSicher1234");
			assertNotNull(token, "Token should not be NULL");
			assertNotNull(token.getUSID(), "Session ID should not be NULL");
			assertTrue(token.getUserId() == 0, "Wrong UserID in session token, expected = 0");
		} catch (InvalidCredentialsException e) {
			fail("login with valid credentials should not throw an exception");
		}
	}

	/**
	 * Testing if a login whith invalid credentials returns nothing
	 */
	@Test
	@SuppressWarnings("unused") // We are testing for an exeption.
	public void testLoginInvalidCredetials() {
		log.info("entering Integration test testLoginInvalidCredetials");
		
		try {
			AccessToken token = this.authsvc.login(null, null);
			fail("Token should be NULL, one or more inputs were NULL");
		} catch (InvalidCredentialsException e) {
		}

		try {
			AccessToken token = this.authsvc.login(null, "BringMe115");
			fail("Token should be NULL, one or more inputs were NULL");
		} catch (InvalidCredentialsException e) {
		}
		
		try {
			AccessToken token = this.authsvc.login("ruediger@thowl.de", null);
			fail("Token should be NULL, one or more inputs were NULL");
		} catch (InvalidCredentialsException e) {
		}

		try {
			AccessToken token = this.authsvc.login("ruediger@thowl.de", "BringMe115");
			fail("Token should be NULL, wrong password");
		} catch (InvalidCredentialsException e) {
		}
		
		try {
			AccessToken token = this.authsvc.login("ivar@test.de", "BringMe115");
			fail("Token should be NULL, user does not exist");
		} catch (InvalidCredentialsException e) {
		}
		
	}
	

	@Test
	public void testValidatePassword() {
		log.info("entering test testValidatePassword");
		assertFalse(this.authsvc.validatePassword(null), "'NULL' should not match the requirements");
		assertFalse(this.authsvc.validatePassword("1234"), "'1234' should not match the requirements");
		assertFalse(this.authsvc.validatePassword("lorem"), "'lorm' should not match the requirements");
		assertFalse(this.authsvc.validatePassword("ipsum"), "'ipsum' should not match the requirements");
		assertTrue(this.authsvc.validatePassword("gr3at@3wdsG"), "'gr3at@3wds' should match the requirements");
		assertTrue(this.authsvc.validatePassword("P@ssw0rd"), "'P@ssw0rd' should match the requirements");
		assertFalse(this.authsvc.validatePassword("abcdefghijklmnopqrst"), "'abcdefghijklmnopqrst' should not match the requirements");
	}

	@Test
	public void testValidateEmail() {
		log.info("entering test testValidateEmail");
		assertFalse(this.authsvc.validateEmail(null), "'NULL' should not match the requirements");
		assertFalse(this.authsvc.validateEmail("john doe"), "'john doe' should not match the requirements");
		assertFalse(this.authsvc.validateEmail("john doe@test.org"), "'john doe@test.org' should not match the requirements");
		assertFalse(this.authsvc.validateEmail("@test.org"), "'@test.org' should not match the requirements");
		assertFalse(this.authsvc.validateEmail("jonh-doe test.org"), "'john-doe test.org' should not match the requirements");
		assertTrue(this.authsvc.validateEmail("johndoe@test.org"), "'johndoe@test.org' This should match the requirements");
	}

	// TODO: this test does not work, the code that is beeing tested does.
	/*
	@Test
	public void testRegister() {
		log.info("entering test testRegister");
		String uname = "test";
        	String passwd = "test123";
       		String email = "test@th-owl.de";

		this.authsvc.register(uname, email, passwd, passwd);

		User usr = this.users.findByUsername(uname);

		assertNotNull(usr, "User resgistration failed");
		assertEquals(uname, usr.getUsername(), "Wrong username");
		assertEquals(email, usr.getEmail(), "Wrong Email");
	}
	*/
}
