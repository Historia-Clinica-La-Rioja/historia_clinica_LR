package net.pladema.clinichistory.requests.servicerequests.domain.observations.exceptions;

public class UnitOfMeasureNotFoundException extends DiagnosticReportObservationException {

	public UnitOfMeasureNotFoundException(Short uomId) {
		super("uom-not-found", "unitOfMeasureId", uomId);
	}

}
