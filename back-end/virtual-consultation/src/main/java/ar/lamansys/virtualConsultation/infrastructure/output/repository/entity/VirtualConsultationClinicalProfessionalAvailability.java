package ar.lamansys.virtualConsultation.infrastructure.output.repository.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import ar.lamansys.virtualConsultation.domain.ClinicalProfessionalAvailabilityBo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Table(name = "virtual_consultation_clinical_professional_availability")
@Entity
public class VirtualConsultationClinicalProfessionalAvailability {

	@Id
	@Column(name = "healthcare_professional_id")
	private Integer healthcareProfessionalId;

	@Column(name = "available", nullable = false)
	private Boolean available;

	public VirtualConsultationClinicalProfessionalAvailability(ClinicalProfessionalAvailabilityBo professionalAvailability) {
		this.healthcareProfessionalId = professionalAvailability.getHealthcareProfessionalId();
		this.available = professionalAvailability.getAvailable();
	}
}
