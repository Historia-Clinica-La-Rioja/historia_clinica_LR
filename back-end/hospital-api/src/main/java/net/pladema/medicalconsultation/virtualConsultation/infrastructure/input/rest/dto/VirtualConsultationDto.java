package net.pladema.medicalconsultation.virtualConsultation.infrastructure.input.rest.dto;

import javax.annotation.Nullable;

import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import net.pladema.medicalconsultation.virtualConsultation.domain.enums.EVirtualConsultationPriority;
import net.pladema.medicalconsultation.virtualConsultation.domain.enums.EVirtualConsultationStatus;
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

	@Nullable
	private String callLink;

	@Nullable
	private Integer availableProfessionalsAmount;

}
