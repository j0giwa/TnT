package de.thowl.tnt.storage.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
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

	@Lob
	@Column(columnDefinition = "mediumblob")
	private byte[] attachment;

	private String mimeType;

	@Transient
	private String encodedAttachment;

	@NotNull
	@NonNull
	private NoteKategory kategory;

	@NotNull
	@NonNull
	@ElementCollection
	private List<String> tags;

	@OneToOne(mappedBy = "note", cascade = CascadeType.REMOVE)
	private SharedNote sharedNote;

}
