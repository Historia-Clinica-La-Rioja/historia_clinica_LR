package net.pladema.establishment.service.domain;

import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class CareLineProblemBo {

	private Integer careLineId;

	private SnomedBo problem;

	public CareLineProblemBo(Integer careLineId, Integer snomedId,
							 String snomedSctid, String snomedPt) {
		this.careLineId = careLineId;
		this.problem = new SnomedBo(snomedId, snomedSctid, snomedPt);
	}
}
