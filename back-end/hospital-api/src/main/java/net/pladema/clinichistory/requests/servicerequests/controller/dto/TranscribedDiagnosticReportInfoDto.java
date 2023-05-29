package net.pladema.clinichistory.requests.servicerequests.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TranscribedDiagnosticReportInfoDto {
	private Integer serviceRequestId;
	private Integer studyId;
	private String studyName;
}
