package net.pladema.medicine.infrastructure.input.rest.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class InstitutionMedicineFinancingStatusDto implements Serializable {

	private static final long serialVersionUID = 2446654484732250647L;

	private Integer id;
	private Integer institutionId;
	private Boolean financedByInstitution;
	private Integer medicineId;
	private String conceptSctid;
	private String conceptPt;
	private Boolean financedByDomain;

}
