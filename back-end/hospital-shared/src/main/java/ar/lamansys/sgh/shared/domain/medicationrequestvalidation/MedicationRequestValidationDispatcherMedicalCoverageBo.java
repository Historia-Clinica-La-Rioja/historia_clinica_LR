package ar.lamansys.sgh.shared.domain.medicationrequestvalidation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicationRequestValidationDispatcherMedicalCoverageBo {

	private Integer id;

	private Integer dispatcherNumber;

	private Integer plan;

	public MedicationRequestValidationDispatcherMedicalCoverageBo(Integer dispatcherNumber, Integer plan) {
		this.dispatcherNumber = dispatcherNumber;
		this.plan = plan;
	}

}
