package ar.lamansys.sgh.shared.infrastructure.input.service.medicationrequestvalidation;

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
public class MedicationRequestValidationDispatcherMedicationDto {

	private Integer articleCode;

	private Short quantity;

	private String diagnose;

}
