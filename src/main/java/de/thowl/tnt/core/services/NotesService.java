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
import de.thowl.tnt.storage.entities.User;

public interface NotesService {

	/**
	 * Adds a new {@link Note} to the Database.
	 *
	 * @param username Name of the {@link User}
	 * @param title    Title of the {@link Note}.
	 * @param subtitle Title of the {@link Note}.
	 * @param content  Content of the {@link Note}
	 * @param type     {@link Type} of the {@link Note}, valid values:
	 *                 {@code text}, {@code audio}, {@code video}
	 * @param kategory {@link Type} of the {@link Note}, valid values:
	 *                 {@code lecture}, {@code litterature}, {@code misc}
	 * @param tags     Whitespace speprated list of keywords assosiated with the
	 *                 {@link Note}
	 */
	public void addNote(String username, String title, String subtitle, String content, String type,
			String kategory,
			String tags);

	public List<Note> getAllNotes(String username);

	public List<Note> getAllNotesByTags(String username, String tags);

	// TODO: remove duplicate
	public void editNote();

	public void delete();
}
