package ar.lamansys.virtualConsultation.infrastructure.input.rest.dto;

import ar.lamansys.virtualConsultation.domain.enums.EVirtualConsultationPriority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VirtualConsultationRequestDto {

	private Integer patientId;

	private Integer clinicalSpecialtyId;

	private Integer careLineId;

	private Integer problemId;

	private EVirtualConsultationPriority priority;

	private Integer motiveId;

}
