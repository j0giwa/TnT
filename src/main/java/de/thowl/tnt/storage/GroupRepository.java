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

import org.springframework.stereotype.Repository;

import de.thowl.tnt.storage.entities.Group;

import org.springframework.data.repository.CrudRepository;

@Repository
public interface GroupRepository extends CrudRepository<Group, Long> {

	/**
	 * Finds a {@link Group} by its ID.
	 *
	 * @param id The ID of the {@link Group} to find.
	 * @return The {@link Group} with the specified ID, or {@code null} if not
	 *         found.
	 */
	public Group findById(int id);

	/**
	 * Finds a {@link Group} by its name.
	 *
	 * @param name The name of the {@link Group} to find.
	 * @return The {@link Group} with the specified name, or {@code null} if not
	 *         found.
	 */
	public Group findByName(String name);
}
