package net.pladema.clinichistory.requests.servicerequests.domain.observations;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.Optional;


@Getter
public class UpdatedDiagnosticReportObservationBo {

	private Integer id;
	private Optional<String> value;
	private Optional<Short> unitOfMeasureId;
	private Optional<BigDecimal> valueNumeric;


	public UpdatedDiagnosticReportObservationBo(Integer id, String value, Short unitOfMeasureId, BigDecimal valueNumeric) {
		this.id = id;
		this.value = Optional.ofNullable(value);
		this.unitOfMeasureId = Optional.ofNullable(unitOfMeasureId);
		this.valueNumeric = Optional.ofNullable(valueNumeric);
	}

	public UpdatedDiagnosticReportObservationBo(Integer id, String value) {
		this.id = id;
		this.value = Optional.ofNullable(value);
		this.unitOfMeasureId = Optional.empty();
	}

}
