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

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.thowl.tnt.storage.entities.Note;
import de.thowl.tnt.storage.entities.NoteKategory;
import de.thowl.tnt.storage.entities.User;

@Repository
public interface NotesRepository extends CrudRepository<Note, Long> {

	/**
	 * Gets a {@link Note} from Database.
	 * 
	 * @param id The id of the {@link Note}.
	 * @return a {@link Note}, or {@code null} if not found.
	 */
	Note findById(long id);

	/**
	 * Gets a list of {@link Note}s, created by a given user, from the Database.
	 * 
	 * @param user The {@link User} who created the {@link Note}s.
	 * @return a list of {@link Note}s, or {@code null} if not found.
	 */
	List<Note> findByUser(User user);

	/**
	 * Gets a list of {@link Note}s, created by a given user, that match a list of
	 * tags, from the Database.
	 * 
	 * @param user The {@link User} who created the {@link Note}s.
	 * @param tags A list of tags that correspond to the {@link Note}.
	 * @return a list of {@link Note}s, or {@code null} if not found.
	 */
	List<Note> findByUserAndTagsIn(User user, List<String> tags);

	/**
	 * Gets a list of {@link Note}s, created by a given user, that are in a specific
	 * kategory, from the Database.
	 * 
	 * @param user     The {@link User} who created the {@link Note}s.
	 * @param kategory The kategory of the {@link Note}.
	 * @return a list of {@link Note}s, or {@code null} if not found.
	 */
	List<Note> findByUserAndKategory(User user, NoteKategory kategory);

	/**
	 * Gets a list of {@link Note}s, created by a given user, that match a list of
	 * tags nd are in a specific kategory, from the Database.
	 * 
	 * @param user     The {@link User} who created the {@link Note}s.
	 * @param tags     A list of tags that correspond to the {@link Note}.
	 * @param kategory The kategory of the {@link Note}.
	 * @return a list of {@link Note}s, or {@code null} if not found.
	 */
	List<Note> findByUserAndKategoryAndTagsIn(User user, NoteKategory kategory, List<String> tags);
}
