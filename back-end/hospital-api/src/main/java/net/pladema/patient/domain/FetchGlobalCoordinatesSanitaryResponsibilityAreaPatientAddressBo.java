package net.pladema.patient.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FetchGlobalCoordinatesSanitaryResponsibilityAreaPatientAddressBo {

	private Integer patientId;

	private String streetName;

	private String houseNumber;

	private String cityName;

	private String stateName;

	private String countryName;

	private String postalCode;

}
