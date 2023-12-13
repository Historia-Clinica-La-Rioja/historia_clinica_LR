package net.pladema.establishment.service.domain;

import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class CareLinePracticeBo {

	Integer careLineId;
	SnomedBo practice;

	public CareLinePracticeBo(Integer careLineId, Integer id, String sctid, String pt){
		this.careLineId = careLineId;
		this.practice = new SnomedBo(id, sctid, pt);
	}
}
