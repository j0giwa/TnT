package de.thowl.tnt.core;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.thowl.tnt.core.services.AuthenticationService;
import de.thowl.tnt.core.services.NotesService;
import de.thowl.tnt.storage.NotesRepository;
import de.thowl.tnt.storage.UserRepository;
import de.thowl.tnt.storage.entities.Note;
import de.thowl.tnt.storage.entities.NoteKategory;
import de.thowl.tnt.storage.entities.NoteType;
import de.thowl.tnt.storage.entities.User;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementaion of the {@link AuthenticationService} interface
 * {@inheritDoc}
 */
@Slf4j
@Service
public class NotesServiceImpl implements NotesService {

	@Autowired
	private UserRepository users;

	@Autowired
	private NotesRepository notes;

	public NoteType setType(String type) {

		switch (type.toLowerCase()) {
			case "text":
			default:
				return NoteType.TEXT;
			case "audio":
				return NoteType.AUDIO;
			case "video":
				return NoteType.VIDEO;
		}
	}

	public NoteKategory setKategory(String kategory) {

		switch (kategory.toLowerCase()) {
			case "lecture":
				return NoteKategory.LECTURE;
			case "litterature":
				return NoteKategory.LITTERATURE;
			case "misc":
			default:
				return NoteKategory.MISC;
		}
	}

	public List<String> formatTags(String tags) {

		String[] tagsArray = tags.split("\\s+");

		// Convert array to List
		return Arrays.asList(tagsArray);
	}

	@Override
	public void add(String username, String title, String subtitle, String content, String type, String kategory,
			String tags) {
		log.debug("entering add");

		User user = users.findByUsername(username);

		Note note = Note.builder().user(user).name(title).subtitle(subtitle).content(content)
				.createdAt(new Date()).type(setType(type)).kategory(setKategory(kategory)).tags(
						formatTags(tags))
				.build();

		this.notes.save(note);
	}

	@Override
	public void edit() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'edit'");
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'delete'");
	}

}
