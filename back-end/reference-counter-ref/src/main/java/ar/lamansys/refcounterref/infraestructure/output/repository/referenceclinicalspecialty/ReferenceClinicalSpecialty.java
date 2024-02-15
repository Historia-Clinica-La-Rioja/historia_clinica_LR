package ar.lamansys.refcounterref.infraestructure.output.repository.referenceclinicalspecialty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reference_clinical_specialty")
@Entity
public class ReferenceClinicalSpecialty {

	@EmbeddedId
	private ReferenceClinicalSpecialtyPk pk;

	public ReferenceClinicalSpecialty(Integer referenceId, Integer clinicalSpecialtyId) {
		this.pk = new ReferenceClinicalSpecialtyPk(referenceId, clinicalSpecialtyId);
	}

}
