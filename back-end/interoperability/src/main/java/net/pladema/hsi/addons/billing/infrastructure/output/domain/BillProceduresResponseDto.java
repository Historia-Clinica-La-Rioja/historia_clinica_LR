package net.pladema.hsi.addons.billing.infrastructure.output.domain;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import net.pladema.hsi.addons.billing.domain.BillProceduresRequestBo;
import net.pladema.hsi.addons.billing.domain.BillProceduresResponseBo;

@Setter
public class BillProceduresResponseDto {

	private @Getter List<BillProceduresResponseItemDto> nonRegisteredPractices;
	private List<BillProceduresResponseTotalsDto> totals;
	private BillProceduresResponseMedicalCoverageDto medicalCoverage;

	private @Getter Float patientTotal;
	private @Getter Float medicalCoverageTotal;
	private @Getter String medicalCoverageCuit;
	private @Getter String medicalCoverageName;

	public BillProceduresResponseDto validate() {
		this.patientTotal = totals.get(0).getPatientTotal();
		this.medicalCoverageTotal = totals.get(0).getMedicalCoverageTotal();
		this.medicalCoverageCuit = medicalCoverage.getCuit();
		this.medicalCoverageName = medicalCoverage.getName();

		return this;

	}

	public BillProceduresResponseBo toBo(BillProceduresRequestBo request) {
		return new BillProceduresResponseBo(
				Collections.emptyList(),//this.getNonRegisteredPractices().stream().map(x->x.toBo()).collect(Collectors.toList()),
				this.getMedicalCoverageTotal(),
				this.getPatientTotal(),
				this.getMedicalCoverageName(),
				this.getMedicalCoverageCuit(),
				true,
				request
		);
	}
}
