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

	private List<BillProceduresResponseItemDto> beds;
	private List<BillProceduresResponseItemDto> practices;
	private List<BillProceduresResponseItemDto> medications;
	private List<BillProceduresResponseItemDto> modules;
	private List<BillProceduresResponseItemDto> nonRegisteredPractices;
	private List<BillProceduresResponseTotalsDto> totals;
	private BillProceduresResponseMedicalCoverageDto medicalCoverage;
	private String medicalCoverageCuit;
	private String medicalCoverageName;

	public BillProceduresResponseBo toBo(BillProceduresRequestBo request, boolean encounterExistsInRemote) {
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
				request,
				encounterExistsInRemote
		);
	}

	private List<BillProceduresResponseItemBo> collectPractices() {
		List<BillProceduresResponseItemBo> ret = new ArrayList<>();
		addAll(ret, this.practices, BillProceduresResponseItemBo.PracticeType.PRACTICE);
		addAll(ret, this.nonRegisteredPractices, BillProceduresResponseItemBo.PracticeType.NON_REGISTERED);
		addAll(ret, this.beds, BillProceduresResponseItemBo.PracticeType.BED);
		addAll(ret, this.medications, BillProceduresResponseItemBo.PracticeType.MEDICATION);
		addAll(ret, this.modules, BillProceduresResponseItemBo.PracticeType.MODULE);
		return ret;
	}

	private void addAll(List<BillProceduresResponseItemBo> ret, List<BillProceduresResponseItemDto> items, BillProceduresResponseItemBo.PracticeType practiceType) {
		if (items != null)
			ret.addAll(items.stream().map(x->x.toBo(practiceType)).collect(Collectors.toList()));
	}

	public BillProceduresResponseDto validate() {
		return this;
	}
}
