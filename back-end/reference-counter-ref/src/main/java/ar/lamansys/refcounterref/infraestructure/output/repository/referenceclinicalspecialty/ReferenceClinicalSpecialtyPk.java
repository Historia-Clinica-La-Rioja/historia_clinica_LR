package ar.lamansys.refcounterref.infraestructure.output.repository.referenceclinicalspecialty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ReferenceClinicalSpecialtyPk implements Serializable {

	private static final long serialVersionUID = -8541615766479944446L;

	@Column(name = "reference_id", nullable = false)
	private Integer referenceId;

	@Column(name = "clinical_specialty_id", nullable = false)
	private Integer clinicalSpecialtyId;

}
