package de.thowl.tnt.storage.entities;

import java.util.Date;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import jakarta.validation.constraints.NotNull;

import lombok.NonNull;
import lombok.ToString;

public abstract class Entry {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	@ToString.Exclude
	private User user;

	@NotNull
	@NonNull
	private String name;

	@NotNull
	@NonNull
	private String content;

	@NotNull
	@NonNull
	private Date date;

}
