package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class SaveMedicationStatementInstitutionalSupplyMedicationDto {

	private String sctid;

	private String pt;

	private Short quantity;

}
