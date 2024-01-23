package net.pladema.hsi.addons.billing.domain;

import java.util.List;

import lombok.Value;

@Value
public class BillProceduresResponseBo {
	List<BillProceduresResponseItemBo> procedures;
	private Float medicalCoverageTotal;
	private Float patientTotal;
	private String medicalCoverageName;
	private String medicalCoverageCuit;
	private boolean enabled;
}
