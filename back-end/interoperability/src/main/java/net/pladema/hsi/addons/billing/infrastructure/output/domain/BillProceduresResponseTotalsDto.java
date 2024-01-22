package net.pladema.hsi.addons.billing.infrastructure.output.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BillProceduresResponseTotalsDto {
	private Integer id;
	private Float medicalCoverageTotal;
	private Float patientTotal;
}
