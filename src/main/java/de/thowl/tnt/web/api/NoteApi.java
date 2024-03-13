package de.thowl.tnt.web.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.thowl.tnt.core.services.NotesService;
import de.thowl.tnt.storage.UserRepository;
import de.thowl.tnt.storage.entities.Note;
import de.thowl.tnt.storage.entities.User;
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
	 * Retrieves a Note from the Database
	 * 
	 * @param apiToken The apitoken of the user.
	 * @param id       The id of the Note.
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
		log.info("entering getNote (GET-Method: /api/note/get)");

		String username;
		User user;
		Note note;

		user = users.findByApiToken(apiToken);
		if (user == null)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unathorised");

		username = user.getUsername();

		note = this.notessvc.getNote(id, username);

		if (note == null)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unathorised");

		return ResponseEntity.status(HttpStatus.OK).body(note);
	}

	/**
	 * @param apiToken The apitoken of the user.
	 * @param id       The id of the task.
	 * 
	 * @return {@code 200} when the task was marked as done,
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
		log.info("entering getAllNotes (GET-Method: /api/note/getall)");

		String username;
		User user;
		List<Note> notes;

		user = users.findByApiToken(apiToken);
		if (user == null)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unathorised");

		username = user.getUsername();
		notes = this.notessvc.getAllNotes(username);

		return ResponseEntity.status(HttpStatus.OK).body(notes);
	}

}
