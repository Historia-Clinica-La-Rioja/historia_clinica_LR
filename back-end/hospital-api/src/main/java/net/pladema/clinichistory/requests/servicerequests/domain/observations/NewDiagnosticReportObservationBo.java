package net.pladema.clinichistory.requests.servicerequests.domain.observations;

import lombok.Getter;

import java.util.Optional;

@Getter
public class NewDiagnosticReportObservationBo {

	private Integer procedureParameterId;
	private String value;

	private Optional<Short> unitOfMeasureId;

	private NewDiagnosticReportObservationBo(Integer procedureParameterId, String value, Short unitOfMeasureId){
		this.procedureParameterId = procedureParameterId;
		this.value = value;
		this.unitOfMeasureId = Optional.ofNullable(unitOfMeasureId);
	}

	public static NewDiagnosticReportObservationBo buildNumeric(Integer procedureParameterId, String value, Short unitOfMeasureId) {
		return new NewDiagnosticReportObservationBo(procedureParameterId, value, unitOfMeasureId);
	}

	public static NewDiagnosticReportObservationBo buildNonNumeric(Integer procedureParameterId, String value) {
		return new NewDiagnosticReportObservationBo(procedureParameterId, value, null);
	}

	public boolean isNumeric() {
		return unitOfMeasureId.isPresent();
	}
}
