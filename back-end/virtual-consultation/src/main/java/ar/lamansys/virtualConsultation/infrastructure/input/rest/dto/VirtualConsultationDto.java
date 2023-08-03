package ar.lamansys.virtualConsultation.infrastructure.input.rest.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import ar.lamansys.virtualConsultation.domain.enums.EVirtualConsultationPriority;
import ar.lamansys.virtualConsultation.domain.enums.EVirtualConsultationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VirtualConsultationDto {

	private Integer id;

	private VirtualConsultationPatientDataDto patientData;

	private String problem;

	private String motive;

	private String clinicalSpecialty;

	private String careLine;

	private VirtualConsultationInstitutionDataDto institutionData;

	private EVirtualConsultationStatus status;

	private VirtualConsultationResponsibleDataDto responsibleData;

	private EVirtualConsultationPriority priority;

	private DateTimeDto creationDateTime;

	private String callId;

}
