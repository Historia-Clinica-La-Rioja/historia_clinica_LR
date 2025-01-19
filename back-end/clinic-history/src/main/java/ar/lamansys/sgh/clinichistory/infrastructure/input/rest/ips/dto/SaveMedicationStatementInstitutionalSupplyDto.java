package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class SaveMedicationStatementInstitutionalSupplyDto {

	private Integer medicationStatementId;

	private List<SaveMedicationStatementInstitutionalSupplyMedicationDto> medications;

}
