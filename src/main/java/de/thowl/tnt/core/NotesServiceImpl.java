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
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.thowl.tnt.core.services.AuthenticationService;
import de.thowl.tnt.core.services.NotesService;
import de.thowl.tnt.storage.NotesRepository;
import de.thowl.tnt.storage.SharedNotesRepository;
import de.thowl.tnt.storage.UserRepository;
import de.thowl.tnt.storage.entities.Note;
import de.thowl.tnt.storage.entities.NoteKategory;
import de.thowl.tnt.storage.entities.SharedNote;
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

	@Autowired
	private SharedNotesRepository sharedNotes;

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

		log.debug("entering formatTags");

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

		User user;
		Note note;

		log.debug("entering add");

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

		this.notes.save(note);
	}

	/**
	 * Encodes the Attachement of the {@link Note}
	 * 
	 * @param notes A list of {@link Note}s
	 * @return A list of {@link Note}s with encoded attachments
	 */
	private List<Note> encodeNotes(List<Note> notes) {
		for (Note note : notes) {
			note.setEncodedAttachment(Base64.getEncoder().encodeToString(note.getAttachment()));
		}
		return notes;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Note getNote(long id) {

		Note note;

		log.debug("entering getNote");

		note = this.notes.findById(id);
		note.setEncodedAttachment(Base64.getEncoder().encodeToString(note.getAttachment()));
		return note;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Note> getAllNotes(String username) {

		User user;
		List<Note> notes;

		log.debug("entering getAllNotes");

		user = users.findByUsername(username);
		notes = encodeNotes(this.notes.findByUser(user));

		return notes;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Note> getNotesByParams(String username, NoteKategory kategory, String tags) {

		User user;
		List<Note> notes;
		List<String> noteTags;

		log.debug("entering getNotesByParams");

		user = users.findByUsername(username);
		noteTags = formatTags(tags);

		if (kategory == NoteKategory.ALL) {
			notes = noteTags.get(0).isEmpty() ? this.notes.findByUser(user)
					: this.notes.findByUserAndTagsIn(user, noteTags);
		} else {
			notes = noteTags.get(0).isEmpty() ? this.notes.findByUserAndKategory(user, kategory)
					: this.notes.findByUserAndKategoryAndTagsIn(user, kategory, noteTags);
		}

		notes = encodeNotes(notes);
		return notes;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void editNote(long id, String username, String title,
			String subtitle, String content,
			byte[] attachment, String mimeType,
			String kategory, String tags) {

		User user;
		Note note;

		log.debug("entering editNote");

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

		this.notes.save(note);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(long id) {

		Note note;

		log.debug("entering delete");

		note = this.notes.findById(id);

		if (null != note) {
			log.info("deleting task id: {}", id);
			this.notes.delete(note);
		}
	}

	@Override
	public void toggleSharing(long noteId) {

		Note note;
		SharedNote shared;

		log.debug("entering startSharing");

		note = this.notes.findById(noteId);

		// Assume it is already shared for easier toggling
		shared = this.sharedNotes.findByNote(note);

		if (shared != null) {
			this.sharedNotes.delete(shared);
		} else {
			shared = new SharedNote(UUID.randomUUID().toString(), note);
			this.sharedNotes.save(shared);
		}
	}

	@Override
	public SharedNote getSharedNote(String id) {

		log.debug("entering getSharedNote");

		return this.sharedNotes.findByGuid(id);
	}
}
