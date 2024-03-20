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

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@ControllerAdvice
public class TntApplicationErrorController implements ErrorController {

	@ExceptionHandler(ResponseStatusException.class)
    	public String showErrorPage(HttpServletRequest request, HttpServletResponse response, Model model, ResponseStatusException e) {
        
		int code;
		HttpStatusCode status;
		String reason, header, message;

		log.info("entering showErrorPage (Method: /error)");

       	 	status = e.getStatusCode();
		code = status.value();
		reason = e.getReason();

		header = "http_" + code + "_header";
		message = "http_" + code + "_message";

		log.error("CODE: {} REASON: {}", code, reason, e);

		model.addAttribute("errorCode", code);
		model.addAttribute("errorHeader", header);
		model.addAttribute("errorMessage", message);

		response.setStatus(code);
        	return "error";
    	}

}