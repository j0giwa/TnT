package de.thowl.notetilus.storage.entities;

import java.util.Date;

import lombok.Data;

@Data
public class AccessToken {
	private String usid; // unique session id
	private Date lastActive;
	private long userId;
}
