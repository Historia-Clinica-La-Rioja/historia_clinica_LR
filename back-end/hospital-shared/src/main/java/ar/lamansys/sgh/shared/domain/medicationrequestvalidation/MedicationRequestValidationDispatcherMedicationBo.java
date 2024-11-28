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

	private Integer snomedId;

	private Integer articleCode;

	private Short quantity;

	private Boolean isCommercialMedication;

	public MedicationRequestValidationDispatcherMedicationBo(Integer articleCode, Short quantity) {
		this.articleCode = articleCode;
		this.quantity = quantity;
	}

}
