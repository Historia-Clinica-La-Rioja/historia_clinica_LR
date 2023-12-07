package net.pladema.medicalconsultation.virtualConsultation.infrastructure.input.rest.dto;

import lombok.Getter;
import lombok.Setter;
import net.pladema.medicalconsultation.virtualConsultation.domain.enums.EVirtualConsultationPriority;
import net.pladema.medicalconsultation.virtualConsultation.domain.enums.EVirtualConsultationStatus;

import javax.annotation.Nullable;

@Getter
@Setter
public class VirtualConsultationFilterDto {

	@Nullable
	private Integer careLineId;

	@Nullable
	private Integer clinicalSpecialtyId;

	@Nullable
	private EVirtualConsultationPriority priority;

	@Nullable
	private Boolean availability;

	@Nullable
	private Integer responsibleHealthcareProfessionalId;

	@Nullable
	private EVirtualConsultationStatus status;

	@Nullable
	private Integer institutionId;

}
