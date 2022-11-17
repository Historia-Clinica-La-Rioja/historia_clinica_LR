package ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class SingleDiagnosticDto {
	private SnomedDto diagnosis;
	private Boolean isMain;
	private String diagnosisType;
	private String diagnosisVerificationStatus;
	private LocalDateTime updatedOn;
}
