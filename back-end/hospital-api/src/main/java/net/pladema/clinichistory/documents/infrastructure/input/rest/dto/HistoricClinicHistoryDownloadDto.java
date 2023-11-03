package net.pladema.clinichistory.documents.infrastructure.input.rest.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HistoricClinicHistoryDownloadDto {

	private Integer id;
	private Integer patientId;
	private Integer institutionId;
	private String user;
	private DateTimeDto downloadDate;

}
