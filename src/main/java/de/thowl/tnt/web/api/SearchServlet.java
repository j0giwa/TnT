package de.thowl.tnt.web.api;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import de.thowl.tnt.core.services.NoteSearchService;
import de.thowl.tnt.storage.entities.Note;
import de.thowl.tnt.storage.entities.NoteKategory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/search")
public class SearchServlet extends HttpServlet{
    @Autowired
    private NoteSearchService searchService;
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query"); // extract the search query
        String filterValue = request.getParameter("filter"); //extract the filtervalue.

        //convert the filtervalue in an enum-constant
        NoteKategory filter = null;
        if(filterValue != null && !filterValue.isEmpty()) {
            filter = NoteKategory.valueOf(filterValue);
            List<Note> matchingsNotes = searchService.searchNotes(query, filter);
        }
    }
}
