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

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import de.thowl.tnt.core.services.NoteSearchService;
import de.thowl.tnt.storage.entities.Note;
import de.thowl.tnt.storage.entities.NoteKategory;
import de.thowl.tnt.storage.entities.User;
import lombok.extern.slf4j.Slf4j;

/**min
 * {@inheritDoc}
 */
@Slf4j
public class NoteSearchServiceImpl implements NoteSearchService {

    @Autowired
    private NotesServiceImpl notesServiceImpl;

    @Autowired
    private User user;
    /**
     * Perform search logic here based on query and filter
     * 
     * 
     * @param query search term
     * @param filter parameter of type "NoteKategory" for the select category filter.
     * @return matchingsNotes
     */
    @Override
    public List<Note> searchNotes(String query, NoteKategory filter) {
        List<Note> matchingsNotes = new ArrayList<>();

        //Assug you have a list of notes.
        List<Note> allNotes = notesServiceImpl.getAllNotes(user.getUsername()); //This method should return all notes from the data source
        log.info(String.valueOf(allNotes));
        //Iterate over all the notes and apply the filter
        for (Note note : allNotes) {
            if(filter == null || note.getKategory() == filter) {
                if (note.getKategory() == filter || note.getContent().contains(query)) {
                    log.info("search for" + query);
                    matchingsNotes.add(note);
                    
                }
            }
        }
        log.info("search success");
        return matchingsNotes;
    }

}