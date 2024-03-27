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

package de.thowl.tnt.web.api;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.thowl.tnt.core.services.NotesService;
import de.thowl.tnt.storage.UserRepository;
import de.thowl.tnt.storage.entities.Note;
import de.thowl.tnt.storage.entities.User;
import de.thowl.tnt.web.api.schema.NoteSchema;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/note")
@Tag(name = "note", description = "note API")
public class NoteApi {

	@Autowired
	private UserRepository users;

	@Autowired
	private NotesService notessvc;

	/**
	 * Adds a new {@link Note} to the Database.
	 * 
	 * @param apiToken The apitoken of the {@link User}.
	 * @param note     The {@link Note} to add.
	 * 
	 * @return {@code 200} when the {@link Note} was added to the database,
	 *         {@code 403} when the requesting user fails to authenticate
	 */
	@Operation(summary = "Adds a new note to the database")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Success", content = @Content),
			@ApiResponse(responseCode = "403", description = "Unauthorised or missing token", content = @Content)
	})
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<String> addNote(@Parameter(description = "Your api token") @RequestParam String apiToken,
			@RequestBody NoteSchema note) {

		long userId;
		User user;
		byte[] fileContent;
		String mimeType;

		log.info("entering addNote (POST-Method: /api/note/add)");

		user = users.findByApiToken(apiToken);

		if (user == null)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unathorised");

		userId = user.getId();
		fileContent = null;
		mimeType = "application/octet-stream";

		try {
			fileContent = note.getFile().getBytes();
			mimeType = note.getFile().getContentType();
		} catch (IOException e) {
		}

		this.notessvc.addNote(userId, note.getTitle(), note.getSubtitle(), note.getContent(), fileContent,
				mimeType, note.getKategory(), note.getTags());

		return ResponseEntity.status(HttpStatus.OK).body("success");
	}

	/**
	 * Retrieves a {@link Note} from the Database
	 * 
	 * @param apiToken The apitoken of the {@link User}.
	 * @param id       The id of the {@link Note}.
	 * 
	 * @return {@code 200} and the {@link Note},
	 *         {@code 403} when the requesting user fails to authenticate.
	 */
	@Operation(summary = "Retrieves a Note from the Database")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Note as json", content = @Content),
			@ApiResponse(responseCode = "403", description = "Unauthorised api token", content = @Content)
	})
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public ResponseEntity<Object> getNote(@Parameter(description = "Your api token") @RequestParam String apiToken,
			@Parameter(description = "The id of the note") @RequestParam long id) {

		long userId;
		User user;
		Note note;

		log.info("entering getNote (GET-Method: /api/note/get)");

		user = users.findByApiToken(apiToken);
		if (user == null)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unathorised");

		userId = user.getId();
		note = this.notessvc.getNote(id, userId);

		if (note == null)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unathorised");

		return ResponseEntity.status(HttpStatus.OK).body(note);
	}

	/**
	 * Gets all {@link Note}s by a specific {@link User} from Database.
	 * 
	 * @param apiToken The apitoken of the {@link User}.
	 * @param id       The id of the {@link Note}.
	 * 
	 * @return {@code 200} when the {@link Note} was marked as done,
	 *         {@code 403} when the requesting user fails to authenticate
	 */
	@Operation(summary = "Retrieves all Notes created by a specific user from the Database")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Task as json", content = @Content),
			@ApiResponse(responseCode = "403", description = "Unauthorised api token", content = @Content)
	})
	@RequestMapping(value = "/getall", method = RequestMethod.GET)
	public ResponseEntity<Object> getAllNotes(
			@Parameter(description = "Your api token") @RequestParam String apiToken) {

		long userId;
		User user;
		List<Note> notes;

		log.info("entering getAllNotes (GET-Method: /api/note/getall)");

		user = users.findByApiToken(apiToken);
		if (user == null)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unathorised");

		userId = user.getId();
		notes = this.notessvc.getAllNotes(userId);

		return ResponseEntity.status(HttpStatus.OK).body(notes);
	}

}
