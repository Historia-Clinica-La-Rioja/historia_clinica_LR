package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.isolationalert.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IsolationStatusMasterDataDto {
	private Short id;
	private String description;
	private Boolean isActive;
}
