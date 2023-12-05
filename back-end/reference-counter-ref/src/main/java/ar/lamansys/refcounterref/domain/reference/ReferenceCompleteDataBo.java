package ar.lamansys.refcounterref.domain.reference;

import ar.lamansys.refcounterref.domain.referenceappointment.ReferenceAppointmentBo;
import ar.lamansys.refcounterref.domain.referenceregulation.ReferenceRegulationBo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@AllArgsConstructor
public class ReferenceCompleteDataBo {

	private ReferenceDataBo reference;

	private ReferencePatientBo patient;

	private ReferenceAppointmentBo appointment;

	private ReferenceRegulationBo regulation;

}
