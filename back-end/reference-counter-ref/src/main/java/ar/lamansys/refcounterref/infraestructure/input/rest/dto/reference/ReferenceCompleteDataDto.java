package ar.lamansys.refcounterref.infraestructure.input.rest.dto.reference;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReferenceCompleteDataDto {

	private ReferenceDataDto reference;

	private ReferenceAppointmentDto appointment;

	private ReferencePatientDto patient;

	private ReferenceRegulationDto regulation;

}
