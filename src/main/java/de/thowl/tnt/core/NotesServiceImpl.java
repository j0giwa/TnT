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

import java.util.Arrays;
import java.util.Base64;
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

	public NoteKategory setKategory(String kategory) {

		switch (kategory.toLowerCase()) {
			case "lecture":
				return NoteKategory.LECTURE;
			case "litterature":
				return NoteKategory.LITTERATURE;
			default:
			case "misc":
				return NoteKategory.MISC;
		}
	}

	/**
	 * Converts a whitespace seperataed string into a List
	 *
	 * @param tags whitespace seperated list of tags
	 *
	 * @return a list of tags
	 */
	public List<String> formatTags(String tags) {

		String[] tagsArray;

		tagsArray = tags.split("\\s+");

		return Arrays.asList(tagsArray);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addNote(String username, String title, String subtitle,
			String content, byte[] attachment, String mimeType,
			String kategory, String tags) {

		log.debug("entering add");

		User user;
		Note note;

		user = users.findByUsername(username);
		note = Note.builder()
				.user(user)
				.name(title)
				.subtitle(subtitle)
				.content(content)
				.attachment(attachment)
				.mimeType(mimeType)
				.createdAt(new Date())
				.kategory(setKategory(kategory))
				.tags(formatTags(tags))
				.build();

		if (null != note) {
			this.notes.save(note);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Note getNote(long id) {

		log.debug("entering getNote");

		Note note;

		note = this.notes.findById(id);

		note.setEncodedAttachment(Base64.getEncoder().encodeToString(note.getAttachment()));

		return note;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Note> getAllNotes(String username) {
		log.debug("entering getAllNotes");

		User user;
		List<Note> notes;

		user = users.findByUsername(username);
		notes = this.notes.findByUser(user);

		for (Note note : notes) {
			note.setEncodedAttachment(Base64.getEncoder().encodeToString(note.getAttachment()));
		}

		return notes;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Note> getAllNotesByTags(String username, String tags) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getAllNoteByTags'");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void editNote(long id, String username, String title, 
			     String subtitle, String content, 
			     byte[] attachment, String mimeType, 
			     String kategory, String tags) {

		log.debug("entering editNote");


		User user;
		Note note;

		user = users.findByUsername(username);
		note = Note.builder()
				.id(id)
				.user(user)
				.name(title)
				.subtitle(subtitle)
				.content(content)
				.attachment(attachment)
				.createdAt(new Date())
				.kategory(setKategory(kategory))
				.tags(formatTags(tags))
				.build();

		if (null != note) {
			this.notes.save(note);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(long id) {

		log.debug("entering delete");

		Note note;
		
		note = this.notes.findById(id);

		if (null != note) {
			log.info("deleting task id: {}", id);
			this.notes.delete(note);
		}
	}

}
