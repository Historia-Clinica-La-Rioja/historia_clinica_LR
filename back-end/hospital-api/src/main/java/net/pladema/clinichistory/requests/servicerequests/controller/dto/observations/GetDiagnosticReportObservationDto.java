package net.pladema.clinichistory.requests.servicerequests.controller.dto.observations;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetDiagnosticReportObservationDto {
	/**
	 * User understandable representation of the observation
	 * Used when showing results of a diagnostic report
	 */
	@AllArgsConstructor
	@Getter
	public static class Representation {
		String description;
		String value;
	}
	Integer id;
	Integer procedureParameterId;
	String value;
	Short unitOfMeasureId;
	Representation representation;
	String snomedSctid;
	String snomedPt;
}
