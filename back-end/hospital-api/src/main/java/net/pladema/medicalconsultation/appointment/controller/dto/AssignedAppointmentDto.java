package net.pladema.medicalconsultation.appointment.controller.dto;

import ar.lamansys.refcounterref.domain.enums.EReferenceClosureType;
import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import ar.lamansys.sgx.shared.dates.controller.dto.TimeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class AssignedAppointmentDto {

	private Integer id;

	private String professionalName;

	private String license;

	private String clinicalSpecialtyName;

	private DateDto date;

	private TimeDto hour;

	private String office;

	private boolean hasAssociatedReference;

	private EReferenceClosureType associatedReferenceClosureType;

}
