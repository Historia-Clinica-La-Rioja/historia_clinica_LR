package ar.lamansys.sgh.clinichistory.domain.ips;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Nullable;

@Getter
@Setter
@NoArgsConstructor
public class PharmacoSummaryBo extends IndicationBo {

	private SnomedBo snomed;

	private DosageBo dosage;

	private Short viaId;

	@Nullable
	private String note;

	public String getSnomedSctid() {
		return getSnomed() != null ? this.snomed.getSctid() : null;
	}

	public String getSnomedPt() {
		return getSnomed() != null ? this.snomed.getPt() : null;
	}

}