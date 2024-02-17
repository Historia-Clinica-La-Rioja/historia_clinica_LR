package net.pladema.loinc.infrastructure.output.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "loinc_system")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LoincSystem implements Serializable {

	@Id
	@Column(name = "id")
	private Short id;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "description_variant", nullable = false)
	private String descriptionVariant;

}

