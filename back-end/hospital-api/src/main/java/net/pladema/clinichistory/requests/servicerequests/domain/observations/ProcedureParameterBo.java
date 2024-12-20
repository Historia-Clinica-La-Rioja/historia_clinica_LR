package net.pladema.clinichistory.requests.servicerequests.domain.observations;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProcedureParameterBo {
	private Integer id;

	private Integer procedureTemplateId;

	private Integer loincId;

	private Short orderNumber;

	private Short typeId;

	private Short inputCount;
	private Boolean isNumeric;
	private Boolean isSnomed;
}
