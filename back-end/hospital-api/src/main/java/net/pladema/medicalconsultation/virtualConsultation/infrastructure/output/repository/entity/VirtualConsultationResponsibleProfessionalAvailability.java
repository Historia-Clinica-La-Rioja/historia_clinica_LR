package net.pladema.medicalconsultation.virtualConsultation.infrastructure.output.repository.entity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import net.pladema.medicalconsultation.virtualConsultation.domain.VirtualConsultationResponsibleProfessionalAvailabilityBo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "virtual_consultation_responsible_professional_availability")
@Getter
@Setter
@NoArgsConstructor
public class VirtualConsultationResponsibleProfessionalAvailability {

	@Id
	@Embedded
	private VirtualConsultationResponsibleProfessionalAvailabilityPK id;

	@Column(name = "available", nullable = false)
	private Boolean available;

	public VirtualConsultationResponsibleProfessionalAvailability(VirtualConsultationResponsibleProfessionalAvailabilityBo professionalAvailability) {
		this.id = new VirtualConsultationResponsibleProfessionalAvailabilityPK(professionalAvailability.getHealthcareProfessionalId(), professionalAvailability.getInstitutionId());
		this.available = professionalAvailability.getAvailable();
	}

}
