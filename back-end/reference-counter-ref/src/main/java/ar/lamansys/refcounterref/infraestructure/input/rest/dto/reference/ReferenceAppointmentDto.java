package ar.lamansys.refcounterref.infraestructure.input.rest.dto.reference;

import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ReferenceAppointmentDto {

	private Integer appointmentId;

	private Short appointmentStateId;

	private ReferenceInstitutionDto institution;

	private DateTimeDto date;

	private String professionalFullName;

	private String authorFullName;

	private DateTimeDto createdOn;

}
