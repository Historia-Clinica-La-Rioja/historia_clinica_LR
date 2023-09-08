package net.pladema.medicalconsultation.virtualConsultation.infrastructure.input.rest.dto;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import net.pladema.medicalconsultation.virtualConsultation.domain.enums.EVirtualConsultationPriority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VirtualConsultationRequestDto {

	@NotNull
	private Integer patientId;

	@NotNull
	private Integer clinicalSpecialtyId;

	@NotNull
	private Integer careLineId;

	@Nullable
	private SnomedDto problem;

	@NotNull
	private EVirtualConsultationPriority priority;

	@NotNull
	private SnomedDto motive;

}
