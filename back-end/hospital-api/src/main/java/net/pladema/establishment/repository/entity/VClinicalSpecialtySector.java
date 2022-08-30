package net.pladema.establishment.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Immutable
@Table(name = "v_clinical_specialty_sector")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class VClinicalSpecialtySector {

	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "sector_id", nullable = false)
	private Integer sectorId;

	@Column(name = "clinical_specialty_id", nullable = false)
	private Integer clinicalSpecialtyId;

	@Column(name = "description", nullable = false)
	private String description;

	public VClinicalSpecialtySector(ClinicalSpecialtySector clinicalSpecialtySector) {
		this.id = clinicalSpecialtySector.getId();
		this.sectorId = clinicalSpecialtySector.getSectorId();
		this.clinicalSpecialtyId = clinicalSpecialtySector.getClinicalSpecialtyId();
		this.description = clinicalSpecialtySector.getDescription();
	}

	public ClinicalSpecialtySector parseToClinicalSpecialtySector() {
		ClinicalSpecialtySector parsedClinicalSpecialtySector = new ClinicalSpecialtySector();
		parsedClinicalSpecialtySector.setId(this.id);
		parsedClinicalSpecialtySector.setSectorId(this.sectorId);
		parsedClinicalSpecialtySector.setClinicalSpecialtyId(this.clinicalSpecialtyId);
		parsedClinicalSpecialtySector.setDescription(this.description);

		return parsedClinicalSpecialtySector;
	}

}
