package de.thowl.tnt.web.api.schema;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class NoteSchema {

	private long id;

	private String title;
	private String subtitle;

	private String content;

	private MultipartFile file;

	private String type;

	private String kategory;

	private String tags;

}
