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

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import de.thowl.tnt.core.services.NotesService;
import de.thowl.tnt.storage.NotesRepository;
import de.thowl.tnt.storage.UserRepository;
import de.thowl.tnt.storage.entities.Note;
import de.thowl.tnt.storage.entities.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
public class TestNoteService {

	private final long NOTEUSERID = 3L;
	private final String NOTEUSER = "Notestester";

	@Autowired
	private NotesService notessvc;

	@Autowired
	private NotesRepository notes;

	@Autowired
	private UserRepository users;

	@Test
	public void testAddNote() {

		long notesAmount;

		log.info("entering test testAddNote");

		notesAmount = this.notes.count();

		this.notessvc.addNote(
				NOTEUSERID, "Test Note", "Test Note Subtile",
				"Test Content", null, null, "misc", "test");
		assertEquals(notesAmount + 1, this.notes.count());
	}

	@Test
	public void testDeleteNote() {

		long notesAmount;

		log.info("entering test testDeleteNote");

		notesAmount = this.notes.count();
		this.notessvc.delete(1, NOTEUSERID);
		assertEquals(notesAmount - 1, this.notes.count());
	}

	// @Test
	// TODO: for some reason this test needs to be isolated to work
	public void testGetAllNotes() {

		User user;
		List<Note> notes;
		List<Note> result;

		log.info("entering test testGetAllNotes");

		user = this.users.findByUsername(NOTEUSER);
		notes = this.notes.findByUser(user);
		result = this.notessvc.getAllNotes(NOTEUSERID);

		assertEquals(notes.size(), result.size(), "These should be the same size");
	}
}
