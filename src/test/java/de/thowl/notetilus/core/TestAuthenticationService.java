package de.thowl.notetilus.core;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.thowl.notetilus.core.services.AuthenticationService;
import de.thowl.notetilus.storage.entities.AccessToken;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class TestAuthenticationService {
	
	@Autowired
	private AuthenticationService authsvc;

	/**
	 * Testing if a login whith valid credentials returns an accesstoken
	 * TODO: This is an Integration Test
	 */
	@Test
	public void testLogin() {
		log.info("entering test testLogin");
		// NOTE: The password of the testuser is concidered isecure for our standards
		AccessToken token = this.authsvc.login("ruediger@thowl.de", "SuperSicher1234");
		assertNotNull(token, "Token should not be NULL");
		assertNotNull(token.getUSID(), "Session ID should not be NULL");
		assertTrue(token.getUserId() == 0, "Wrong UserID in session token, expected = 0");
	}

	/**
	 * Testing if a login whith invalid credentials returns nothing
	 * TODO: This is an Integration Test
	 */
	@Test
	public void testLoginInvalidCredetials() {
		log.info("entering test testLoginInvalidCredetials");
		AccessToken token = this.authsvc.login("ruediger@thowl.de", "BringMe115");
		assertNull(token, "Token should be NULL");
	}
	
	@Test
	public void testValidatePassword() {
		assertFalse(this.authsvc.validatePassword("1234"), "'1234' should not match the requirements");
		assertFalse(this.authsvc.validatePassword("lorem"), "'lorm' should not match the requirements");
		assertFalse(this.authsvc.validatePassword("ipsum"), "'ipsum' should not match the requirements");
		assertTrue(this.authsvc.validatePassword("gr3at@3wdsG"), "'gr3at@3wds' should match the requirements");
		assertTrue(this.authsvc.validatePassword("P@ssw0rd"), "'P@ssw0rd' should match the requirements");
		assertFalse(this.authsvc.validatePassword("abcdefghijklmnopqrst"), "'abcdefghijklmnopqrst' should not match the requirements");
	}

	@Test
	public void testValidateEmail() {
		assertFalse(this.authsvc.validateEmail("john doe"), "'john doe' should not match the requirements");
		assertFalse(this.authsvc.validateEmail("john doe@test.org"), "'john doe@test.org' should not match the requirements");
		assertFalse(this.authsvc.validateEmail("@test.org"), "'@test.org' should not match the requirements");
		assertFalse(this.authsvc.validateEmail("jonh-doe test.org"), "'john-doe test.org' should not match the requirements");
		assertTrue(this.authsvc.validateEmail("johndoe@test.org"), "'johndoe@test.org' This should match the requirements");
	}
}
