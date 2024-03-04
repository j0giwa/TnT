package de.thowl.tnt.storage.entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "Notes")
@SuperBuilder
@NoArgsConstructor
public class Note extends Entry {

	@NotNull
	@NonNull
	private String subtitle;

	@NotNull
	@NonNull
	private NoteType type;

	@NotNull
	@NonNull
	private NoteKategory kategory;

	@NotNull
	@NonNull
	private List<String> tags;

}
