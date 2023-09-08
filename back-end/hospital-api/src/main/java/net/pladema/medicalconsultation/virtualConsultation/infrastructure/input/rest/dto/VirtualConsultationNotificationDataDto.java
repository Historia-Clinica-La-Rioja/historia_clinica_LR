package net.pladema.medicalconsultation.virtualConsultation.infrastructure.input.rest.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import net.pladema.medicalconsultation.virtualConsultation.domain.enums.EVirtualConsultationPriority;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VirtualConsultationNotificationDataDto {

	private Integer virtualConsultationId;

	private String patientName;

	private String patientLastName;

	private EVirtualConsultationPriority priority;

	private DateTimeDto creationDateTime;

	private String responsibleFirstName;

	private String responsibleLastName;

	private Integer responsibleUserId;

	private String clinicalSpecialty;

	private String institutionName;

	private String callLink;

}
