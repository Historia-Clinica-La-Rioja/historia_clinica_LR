package ar.lamansys.sgh.clinichistory.domain.hce.summary;

import ar.lamansys.sgh.clinichistory.domain.ips.ClinicalTerm;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MedicationSummaryBo extends ClinicalTerm {

	private Integer id;

	private Integer encounterId;

	public MedicationSummaryBo(Integer id, Integer encounterId, Integer snomedId, String sctid, String pt) {
		this.id = id;
		this.encounterId = encounterId;
		this.setSnomed(new SnomedBo(snomedId, sctid, pt));
	}

	public String getSnomedSctid() {
		return this.getSnomed().getSctid();
	}
	public String getSnomedPt() {
		return this.getSnomed().getPt();
	}
}
