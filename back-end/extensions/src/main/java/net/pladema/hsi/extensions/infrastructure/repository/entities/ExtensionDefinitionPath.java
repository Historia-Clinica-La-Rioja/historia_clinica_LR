package net.pladema.hsi.extensions.infrastructure.repository.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "extension_definition_path")
@Getter
@Setter
@NoArgsConstructor
public class ExtensionDefinitionPath {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Short id;

	@Column(name = "path")
	private String path;

	@Column(name = "name")
	private String name;

}
