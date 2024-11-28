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
public class MedicationRequestValidationDispatcherProfessionalBo {

	private Integer id;

	private String name;

	private String lastName;

	private String identificationType;

	private String identificationNumber;

	//TODO: Matricula

}
