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
public class MedicationRequestValidationDispatcherInstitutionBo {

	private String name;

	private String address;

	private String cuit;

	public MedicationRequestValidationDispatcherInstitutionBo(String name, String cuit, String streetName, String homeNumber,
															  String cityName, String stateName) {
		this.name = name;
		this.cuit = cuit;
		this.address = String.join(" ",
				streetName != null ? streetName : "",
				homeNumber != null ? homeNumber : "",
				cityName != null ? cityName : "",
				stateName != null ? stateName : "");
	}

}
