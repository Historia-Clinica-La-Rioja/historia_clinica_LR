package net.pladema.cipres.infrastructure.output.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "cipres_clinical_specialty")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CipresClinicalSpecialty implements Serializable {

	private static final long serialVersionUID = -8213478724156496358L;

	@EmbeddedId
	public CipresClinicalSpecialtyPk pk;

}
