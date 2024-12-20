package net.pladema.clinichistory.requests.servicerequests.domain.observations;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.Optional;

@Getter
public class NewDiagnosticReportObservationBo {

	private Integer procedureParameterId;
	private Optional<String> value;
	private Optional<Short> unitOfMeasureId;
	private Optional<BigDecimal> valueNumeric;

	private NewDiagnosticReportObservationBo(Integer procedureParameterId, String value, Short unitOfMeasureId, BigDecimal valueNumeric){
		this.procedureParameterId = procedureParameterId;
		this.value = Optional.ofNullable(value);
		this.unitOfMeasureId = Optional.ofNullable(unitOfMeasureId);
		this.valueNumeric = Optional.ofNullable(valueNumeric);
	}

	public static NewDiagnosticReportObservationBo buildNumeric(Integer procedureParameterId, String value, Short unitOfMeasureId, BigDecimal valueNumeric) {
		return new NewDiagnosticReportObservationBo(procedureParameterId, value, unitOfMeasureId, valueNumeric);
	}

	public static NewDiagnosticReportObservationBo buildNonNumeric(Integer procedureParameterId, String value) {
		return new NewDiagnosticReportObservationBo(procedureParameterId, value, null, null);
	}

	public boolean isNumeric() {
		return unitOfMeasureId.isPresent();
	}
}
