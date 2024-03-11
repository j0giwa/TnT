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

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.thowl.tnt.core.services.ShareService;
import de.thowl.tnt.storage.NotesRepository;
import de.thowl.tnt.storage.SharedNotesRepository;
import de.thowl.tnt.storage.entities.Note;
import de.thowl.tnt.storage.entities.SharedNote;
import lombok.extern.slf4j.Slf4j;


/**
 * Implementaion of the {@link ShareService} interface
 * {@inheritDoc}
 */
@Slf4j
@Service
public class ShareServiceImpl implements ShareService {

	@Autowired
	private NotesRepository notes;

	@Autowired
	private SharedNotesRepository sharedNotes;

	@Override
	public void startSharing(long noteId) {

		log.debug("entering startSharing");

		Note note;
		SharedNote shared;

		note = this.notes.findById(noteId);
		shared = new SharedNote(UUID.randomUUID().toString(), note);

		this.sharedNotes.save(shared);

	}
	
	@Override
	public SharedNote getSharedNote(String id) {
		
		log.debug("entering getSharedNote");

		return this.sharedNotes.findByGuid(id);
	}


}
 
