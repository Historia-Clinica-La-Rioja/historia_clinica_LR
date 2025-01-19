package ar.lamansys.refcounterref.domain.referenceappointment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
public class ReferenceAppointmentSummaryBo {

	private Integer referenceId;
	private Short appointmentStateId;
	private Short referenceClosureTypeId;

}
