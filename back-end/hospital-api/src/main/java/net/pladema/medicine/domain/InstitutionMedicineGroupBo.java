package net.pladema.medicine.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class InstitutionMedicineGroupBo implements Serializable {

	private static final long serialVersionUID = 2446654484732250647L;

	private Integer id;
	private Integer medicineGroupId;
	private Integer institutionId;
	private String name;
	private Boolean isDomain;
	private Boolean requiresAudit;
	private Boolean outpatient;
	private Boolean internment;
	private Boolean emergencyCare;
	private Boolean message;
	private Boolean allDiagnoses;
	private Boolean enabled;

	public InstitutionMedicineGroupBo(Integer id, Integer medicineGroupId, Integer institutionId, String name, Boolean isDomain, Boolean deleted)
	{
		this.id = id;
		this.medicineGroupId = medicineGroupId;
		this.institutionId = institutionId;
		this.name = name;
		this.isDomain = isDomain;
		this.enabled = !deleted;
	}

}
