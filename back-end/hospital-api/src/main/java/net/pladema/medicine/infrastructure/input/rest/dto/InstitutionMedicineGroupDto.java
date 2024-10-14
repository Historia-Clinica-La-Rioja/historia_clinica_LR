package net.pladema.medicine.infrastructure.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InstitutionMedicineGroupDto implements Serializable {

	private static final long serialVersionUID = 2446654484732250647L;

	private Integer id;
	private Integer medicineGroupId;
	private Integer institutionId;
	private String name;
	private Boolean enabled;
	private Boolean isDomain;
	private Boolean requiresAudit;
	private Boolean outpatient;
	private Boolean internment;
	private Boolean emergencyCare;
	private String message;
	private Boolean allDiagnoses;
	private String requiredDocumentation;

}
