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

package de.thowl.tnt.storage;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import de.thowl.tnt.storage.SessionRepository;
import de.thowl.tnt.storage.entities.Session;
import de.thowl.tnt.storage.entities.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
class TestSessionRepository {

    @Autowired
    private SessionRepository sessions;

    @Test
    void testStoreSession() {
        log.debug("entering testStoreSession()");
        final String sessionId = "1234567890";
        User usr = new User();
        usr.setId(1);
        usr.setUsername("Keres");
        usr.setEmail("godlike@thyart.web");
        usr.setPassword("joinM3In4rmageddon");
        Session session = new Session(sessionId, usr);

        this.sessions.save(session);
        assertNotNull(this.sessions.findByAuthToken(sessionId));
    }

}