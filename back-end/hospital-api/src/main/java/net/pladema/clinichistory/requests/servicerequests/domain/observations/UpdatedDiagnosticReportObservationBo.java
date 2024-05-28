package net.pladema.clinichistory.requests.servicerequests.domain.observations;

import lombok.Getter;

import java.util.Optional;


@Getter
public class UpdatedDiagnosticReportObservationBo {

	private Integer id;
	private String value;
	private Optional<Short> unitOfMeasureId;


	public UpdatedDiagnosticReportObservationBo(Integer id, String value, Short unitOfMeasureId) {
		this.id = id;
		this.value = value;
		this.unitOfMeasureId = Optional.ofNullable(unitOfMeasureId);
	}

	public UpdatedDiagnosticReportObservationBo(Integer id, String value) {
		this.id = id;
		this.value = value;
		this.unitOfMeasureId = Optional.empty();
	}

}
