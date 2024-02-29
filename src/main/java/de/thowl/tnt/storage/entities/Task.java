package de.thowl.tnt.storage.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "Tasks")
@NoArgsConstructor
@RequiredArgsConstructor
public class Task extends Entry {

	@NotNull
	@NonNull
	private Priority priorty;

}
