package net.pladema.medicine.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class InstitutionMedicineFinancingStatusBo implements Serializable {

	private static final long serialVersionUID = 2446654484732250647L;

	private Integer id;
	private Integer institutionId;
	private Boolean financedByInstitution;
	private Integer medicineId;
	private String conceptSctid;
	private String conceptPt;
	private Boolean financedByDomain;

}
