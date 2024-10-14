package ar.lamansys.sgh.publicapi.patient.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PatientPrescriptionAddressBo {
	String country;
	String province;
	String department;
	String city;
	String street;
	String streetNumber;
}

