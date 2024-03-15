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

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.thowl.tnt.storage.entities.AccessToken;
import de.thowl.tnt.storage.entities.Session;

@Repository
public interface SessionRepository extends CrudRepository<Session, Long> {

	/**
	 * Finds a {@link Session} by its ID.
	 *
	 * @param id The ID of the {@link Session} to find.
	 * @return The {@link Session} with the specified ID, or {@code null} if not
	 *         found.
	 */
	public Session findById(long id);

	/**
	 * Finds a {@link Session} by its {@link AccessToken}.
	 *
	 * @param authToken The {@link AccessToken} of the session to find.
	 * @return The {@link Session} with the specified authentication token, or
	 *         {@code null} if not found.
	 */
	public Session findByAuthToken(String authToken);

	/**
	 * Finds a {@link Session} by the {@link User}'s ID it belongs to.
	 *
	 * @param userId The ID of the {@link User} to whom the {@link Session} belongs.
	 * @return The {@link Session} associated with the specified user ID, or
	 *         {@code null} if not found.
	 */
	public Session findByUserId(long userId);

	/**
	 * Finds {@link Session} whose expiration time is before the given current time.
	 *
	 * @param currentTime The current time used for comparison.
	 * @return A list of {@link Session} whose expiration time is before the given
	 *         current
	 *         time, or {@code null} if not found.
	 */
	List<Session> findByExpiresAtBefore(Date currentTime);
}
