package ar.lamansys.sgh.clinichistory.domain.ips;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class SaveMedicationStatementInstitutionalSupplyMedicationBo {

	private Integer snomedId;

	private String sctid;

	private String pt;

	private Short quantity;

}
