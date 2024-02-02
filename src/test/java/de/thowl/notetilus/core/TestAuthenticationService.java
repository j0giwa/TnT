package de.thowl.notetilus.core;

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

    @Test
    public void testLogin() {
        log.info("entering test testLogin");
        AccessToken token = this.authsvc.login("ruediger@thowl.de", "SuperSicher1234");
        assertNotNull(token, "Token should not be NULL");
        assertNotNull(token.getUSID(), "Session ID should not be NULL");
        assertTrue(token.getUser_id() == 1, "Wrong UserID in session token, expected = 1");
    }

    @Test
    public void testLoginInvalidCredetials() {
        log.info("entering test testLoginInvalidCredetials");
        AccessToken token = this.authsvc.login("ruediger@thowl.de", "sylosis");
        assertNull(token, "Token should be NULL");
    }
}
