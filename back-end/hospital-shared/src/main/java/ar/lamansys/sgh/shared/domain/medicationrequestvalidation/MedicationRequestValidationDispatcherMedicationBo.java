package ar.lamansys.sgh.shared.domain.medicationrequestvalidation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MedicationRequestValidationDispatcherMedicationBo {

	private Integer articleCode;

	private Short packageQuantity;

	private String diagnose;

	public MedicationRequestValidationDispatcherMedicationBo(Short packageQuantity, String diagnose) {
		this.packageQuantity = packageQuantity;
		this.diagnose = diagnose;
	}
}
