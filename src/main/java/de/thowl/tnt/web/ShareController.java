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

package de.thowl.tnt.web;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import de.thowl.tnt.core.services.ShareService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ShareController {

	@Autowired
	private ShareService sharesvc;

	@GetMapping("/share/{uuid}")
	public String showSharePage(@PathVariable("uuid") UUID uuid, Model model) {
		log.info("entering showNotePage (GET-Method: /notes)");

		model.addAttribute("note", this.sharesvc.getSharedNote(uuid));

		return "share";
	}

	@GetMapping("/share/add/{id}")
	public String showSharePage(@PathVariable("id") long id, Model model) {
		log.info("entering showNotePage (GET-Method: /notes)");

		this.sharesvc.startSharing(id);

		return "share";
	}

}

