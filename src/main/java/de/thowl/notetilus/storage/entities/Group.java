package de.thowl.notetilus.storage.entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "Groups")
@NoArgsConstructor
public class Group {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;

	@NotNull
	private String name;

	@NotNull
	private boolean admin;

	@OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
	public List<User> members;

}
