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

package de.thowl.tnt.core.services;

import java.util.List;

import de.thowl.tnt.storage.entities.Note;
import de.thowl.tnt.storage.entities.SharedNote;
import de.thowl.tnt.storage.entities.NoteKategory;
import de.thowl.tnt.storage.entities.User;

public interface NotesService {

	/**
	 * Adds a new {@link Note} to the Database.
	 *
	 * @param userId     The ID of the {@link User} creating the {@link Note}.
	 * @param title      Title of the {@link Note}.
	 * @param subtitle   Title of the {@link Note}.
	 * @param content    Content of the {@link Note}.
	 * @param attachment File to attach to the the {@link Note}.
	 * @param mimeType   MIMEtype of the file.
	 * @param kategory   {@link NoteKategory} of the {@link Note}, valid values:
	 *                   {@code lecture}, {@code litterature}, {@code misc}.
	 * @param tags       Whitespace speprated list of keywords assosiated with the
	 *                   {@link Note}.
	 */
	public void addNote(long userId, String title, String subtitle,
			String content, byte[] attachment, String mimeType, String kategory, String tags);

	/**
	 * Gets a single {@link Note} from Database.
	 * 
	 * <b>!!! WARNING: THIS DOES NOT CHECK OWNERSHIP !!!</b>
	 * USED ONLY IN {@link de.thowl.tnt.web.ShareController} FOR PUBLIC NOTES.
	 * 
	 * @param id The id of the {@link Note}.
	 * @return a {@link Note}
	 */
	public Note getNote(long id);

	/**
	 * Gets a single {@link Note} from Database.
	 * 
	 * @param id     The ID of the {@link Note}.
	 * @param userId The ID of the {@link User} to verify ownership
	 * @return a {@link Note}
	 */
	public Note getNote(long id, long userId);

	/**
	 * Gets all {@link Note}s by a specific {@link User} from Database.
	 * 
	 * @param userId The ID of the {@link User} who created the {@link Note}s.
	 * @return a {@link Note}
	 */
	public List<Note> getAllNotes(long userId);

	/**
	 * Gets all matching {@link Note}s from the Database.
	 * 
	 * @param userId   The ID of the {@link User} who created the {@link Note}s.
	 * @param kategory {@link NoteKategory} of the {@link Note}.
	 * @param tags     Whitespace speprated list of keywords assosiated with the
	 *                 {@link Note}.
	 * @return a list of{@link Note}
	 */
	public List<Note> getNotesByParams(long userId, NoteKategory kategory, String tags);

	/**
	 * Overwrites a {@link Note} in the Database.
	 *
	 * @param id         The id of the {@link Note} to Overwrite
	 * @param userId     The ID of the {@link User} to verify ownership
	 * @param title      New title of the {@link Note}.
	 * @param subtitle   New title of the {@link Note}.
	 * @param content    New content of the {@link Note}
	 * @param attachment New File to attach to the the {@link Note}.
	 * @param mimeType   New MIMEtype of the file.
	 * @param kategory   New {@link Type} of the {@link Note}, valid values:
	 *                   {@code lecture}, {@code litterature}, {@code misc}
	 * @param tags       New whitespace speprated list of keywords assosiated with
	 *                   the
	 *                   {@link Note}
	 */
	public void editNote(long id, long userId, String title, String subtitle,
			String content, byte[] attachment, String mimeType, String kategory, String tags);

	/**
	 * Deletes a {@link Note} from the Database.
	 * 
	 * @param id     The ID of the {@link Note}
	 * @param userId The ID of the {@link User} to verify ownership
	 */
	public void delete(long id, long userId);

	/**
	 * Creates/deletes a {@link SharedNote} from a {@link Note} in the Database.
	 * 
	 * @param id     the ID of the {@link Note} to share
	 * @param userId The ID of the {@link User} to verify ownership
	 */
	public void toggleSharing(long noteId, long userId);

	/**
	 * Retrieves a {@link SharedNote} from Database.
	 * 
	 * @param id the id of the {@link SharedNote}.
	 * 
	 * @return a {@link SharedNote}
	 */
	public SharedNote getSharedNote(String id);

}
