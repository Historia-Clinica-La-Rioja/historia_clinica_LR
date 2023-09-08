package net.pladema.medicalconsultation.virtualConsultation.infrastructure.input.rest.dto;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;

@Getter
@Setter
public class VirtualConsultationFilterDto {

	@Nullable
	private Integer careLineId;

	@Nullable
	private Integer clinicalSpecialtyId;

	@Nullable
	private Integer priorityId;

	@Nullable
	private Boolean availability;

	@Nullable
	private Integer responsibleHealthcareProfessionalId;

	@Nullable
	private Integer statusId;

	@Nullable
	private Integer institutionId;

}
