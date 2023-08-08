package ar.lamansys.virtualConsultation.infrastructure.input.rest.dto;

import ar.lamansys.virtualConsultation.domain.enums.EVirtualConsultationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VirtualConsultationStatusDataDto {

	private Integer virtualConsultationId;

	private EVirtualConsultationStatus status;

}
