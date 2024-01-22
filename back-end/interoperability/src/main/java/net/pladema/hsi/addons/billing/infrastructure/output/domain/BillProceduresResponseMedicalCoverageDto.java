package net.pladema.hsi.addons.billing.infrastructure.output.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BillProceduresResponseMedicalCoverageDto {
	private String name;
	private String cuit;
}
