package de.thowl.tnt.web.forms;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class NoteForm {

	private long id;
	private String title;
	private String subtitle;
	private String content;
	private MultipartFile file;
	private String type;
	private String kategory;
	private String tags;
	private String query;
	private String link;

}
