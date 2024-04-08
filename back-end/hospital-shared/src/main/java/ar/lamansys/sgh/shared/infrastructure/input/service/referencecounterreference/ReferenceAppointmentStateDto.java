package ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReferenceAppointmentStateDto {

	private Integer referenceId;

	private Short appointmentStateId;

	private Short referenceClosureTypeId;

	public ReferenceAppointmentStateDto (Integer referenceId, Short appointmentStateId){
		this.referenceId = referenceId;
		this.appointmentStateId = appointmentStateId;
	}

}
