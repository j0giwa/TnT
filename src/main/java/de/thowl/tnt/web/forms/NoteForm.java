package de.thowl.tnt.web.forms;

import java.util.List;

import lombok.Data;

@Data
public class NoteForm {

	private long id;

	private String title;
	private String subtitle;

	private String content;

	private String type;

	private String kategory;

	private String tags;

}
