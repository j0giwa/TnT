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
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@ControllerAdvice
public class tntErrorController implements ErrorController {

	@RequestMapping(value = "/error")
	public String handleError(HttpServletRequest request, Model model) {

		String code, header, message;

		code = request.getAttribute("code").toString();
		header = request.getAttribute("header").toString();
		message = request.getAttribute("message").toString();

		model.addAttribute("errorCode", code);
		model.addAttribute("errorHeader", header);
		model.addAttribute("errorMessage", message);

		return "error";
	}

	@ExceptionHandler(AccessDeniedException.class)
	public String handleAccessDeniedException(HttpServletRequest request, Model model, AccessDeniedException e) {

		log.error("Access Denied", e);

		request.setAttribute("code", "401: Unauthorized");
		request.setAttribute("header", "Hold Up!");
		request.setAttribute("message", "You need to be logged in to access this resource");

		return handleError(request, model);
	}

	@ExceptionHandler(JpaSystemException.class)
	public String handleJpaSystemException(HttpServletRequest request, Model model, JpaSystemException e) {

		log.error("SQL Error", e);

		request.setAttribute("code", "500: Internal Server Error");
		request.setAttribute("header", "Whoops! Something went Wrong :(");
		request.setAttribute("message", "Have you tried turning it of and on again?");

		return handleError(request, model);
	}
}