package net.pladema.medicine.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MedicineFinancingStatusFilterBo {

	private String conceptSctid;
	private String conceptPt;
	private Boolean financedByDomain;
	private Boolean financedByInstitution;

}
