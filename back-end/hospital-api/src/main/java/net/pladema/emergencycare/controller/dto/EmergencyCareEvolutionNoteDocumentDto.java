package net.pladema.emergencycare.controller.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.Getter;
import lombok.Setter;
import net.pladema.staff.controller.dto.HealthcareProfessionalDto;

@Getter
@Setter
public class EmergencyCareEvolutionNoteDocumentDto {

	private Long documentId;

	private String fileName;

	private String clinicalSpecialtyName;

	private HealthcareProfessionalDto professional;

	private DateTimeDto performedDate;

	private EmergencyCareEvolutionNoteClinicalData emergencyCareEvolutionNoteClinicalData;

	private DateTimeDto editedOn;

	private HealthcareProfessionalDto editor;

}
