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

package de.thowl.tnt.web.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/teapot")
@Tag(name = "teapot", description = "teapot API")
public class TeaPotapi {

	/**
	 * This is absolutely useless and exists just for fun
	 * 
	 * @return HTTP-Status 418 "I'm a teapot"
	 */
	@Operation(summary = "I'm a teapot")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "418", description = "I'm a teapot", content = @Content)
	})
	@GetMapping("/")
	public ResponseEntity<String> teapot() {
		return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body("IM A TEAPOT");
	}
}
