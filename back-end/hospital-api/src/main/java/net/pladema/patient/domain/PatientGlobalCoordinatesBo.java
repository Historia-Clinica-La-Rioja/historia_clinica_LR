package net.pladema.patient.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain.enums.ENominatimResponseCode;

@Getter
@Setter
@AllArgsConstructor
public class PatientGlobalCoordinatesBo {

	private ENominatimResponseCode nominatimResponseCode;
	
	private Integer patientId;

	private Double latitude;

	private Double longitude;

	public PatientGlobalCoordinatesBo(ENominatimResponseCode nominatimResponseCode, Integer patientId) {
		this.nominatimResponseCode = nominatimResponseCode;
		this.patientId = patientId;
	}
	
}
