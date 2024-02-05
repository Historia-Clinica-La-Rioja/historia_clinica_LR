package net.pladema.hsi.addons.billing.infrastructure.output.domain.response;

import lombok.Setter;
import net.pladema.hsi.addons.billing.domain.BillProceduresRequestBo;
import net.pladema.hsi.addons.billing.domain.BillProceduresResponseBo;
import net.pladema.hsi.addons.billing.domain.BillProceduresResponseItemBo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Setter
public class BillProceduresResponseDto {
	/**
	 * private List<EncounterItemSummaryBo> beds;
	 * 	private List<EncounterItemSummaryBo> practices;
	 * 	private List<EncounterItemSummaryBo> medications;
	 * 	private List<EncounterItemSummaryBo> modules;
	 * 	private List<EncounterItemSummaryBo> nonRegisteredPractices;
	 * 	private List<BackofficeEncounterTotal> totals;
	 * 	private MedicalCoverage medicalCoverage;
	 * 	private Person patient;
	 */

	private List<BillProceduresResponseItemDto> beds;
	private List<BillProceduresResponseItemDto> practices;
	private List<BillProceduresResponseItemDto> medications;
	private List<BillProceduresResponseItemDto> modules;
	private List<BillProceduresResponseItemDto> nonRegisteredPractices;
	private List<BillProceduresResponseTotalsDto> totals;
	private BillProceduresResponseMedicalCoverageDto medicalCoverage;
	private String medicalCoverageCuit;
	private String medicalCoverageName;

	public BillProceduresResponseBo toBo(BillProceduresRequestBo request) {
		Float patientTotal = null;
		Float medicalCoverageTotal = null;
		if (totals != null && !totals.isEmpty()) {
			patientTotal = totals.get(0).getPatientTotal();
			medicalCoverageTotal = totals.get(0).getMedicalCoverageTotal();
		}

		return new BillProceduresResponseBo(
				collectPractices(),
				medicalCoverageTotal,
				patientTotal,
				this.medicalCoverageName,
				this.medicalCoverageCuit,
				true,
				request
		);
	}

	private List<BillProceduresResponseItemBo> collectPractices() {
		List<BillProceduresResponseItemBo> ret = new ArrayList<>();
		if (this.practices != null)
			ret.addAll(this.practices.stream().map(x->x.toBo()).collect(Collectors.toList()));
		if (this.nonRegisteredPractices != null)
			ret.addAll(this.nonRegisteredPractices.stream().map(x->x.toBo()).collect(Collectors.toList()));
		if (this.beds != null)
			ret.addAll(this.beds.stream().map(x->x.toBo()).collect(Collectors.toList()));
		if (this.medications != null)
			ret.addAll(this.medications.stream().map(x->x.toBo()).collect(Collectors.toList()));
		if (this.modules != null)
			ret.addAll(this.modules.stream().map(x->x.toBo()).collect(Collectors.toList()));
		return ret;
	}

	public BillProceduresResponseDto validate() {
		return this;
	}
}
