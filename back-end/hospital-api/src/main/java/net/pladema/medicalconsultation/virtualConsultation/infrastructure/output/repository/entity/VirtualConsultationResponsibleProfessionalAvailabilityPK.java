package net.pladema.medicalconsultation.virtualConsultation.infrastructure.output.repository.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class VirtualConsultationResponsibleProfessionalAvailabilityPK implements Serializable {

	@Column(name = "healthcare_professional_id", nullable = false)
	private Integer healthcareProfessionalId;

	@Column(name = "institution_id", nullable = false)
	private Integer institutionId;

}
