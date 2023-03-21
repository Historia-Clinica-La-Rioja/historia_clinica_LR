package net.pladema.patient.controller.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Builder
@ToString
public class AuditablePatientInfoDto {

	private String message;

	private String institutionName;

	private DateTimeDto createdOn;

	private String createdBy;

}
