package ar.lamansys.refcounterref.domain.reference;

import ar.lamansys.refcounterref.domain.counterreference.CounterReferenceSummaryBo;
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

	private ReferenceObservationBo observation;

	private ReferenceForwardingBo forwarding;

	private CounterReferenceSummaryBo closure;

	public ReferenceCompleteDataBo(ReferenceDataBo reference, ReferencePatientBo patient,
								   ReferenceAppointmentBo appointment, ReferenceRegulationBo regulation,
								   ReferenceObservationBo observation, CounterReferenceSummaryBo closure) {
		this.reference = reference;
		this.patient = patient;
		this.appointment = appointment;
		this.regulation = regulation;
		this.observation = observation;
		this.closure = closure;
	}

}
