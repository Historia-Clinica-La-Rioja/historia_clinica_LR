package ar.lamansys.sgh.clinichistory.domain.ips;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class SaveMedicationStatementInstitutionalSupplyBo {

	private Integer institutionId;

	private Integer medicationStatementId;

	private List<SaveMedicationStatementInstitutionalSupplyMedicationBo> medications;

}
