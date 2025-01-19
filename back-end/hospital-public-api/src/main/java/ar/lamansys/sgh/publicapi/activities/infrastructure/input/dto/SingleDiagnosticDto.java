package ar.lamansys.sgh.publicapi.activities.infrastructure.input.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class SingleDiagnosticDto {
	private SnomedCIE10Dto diagnosis;
	private Boolean isMain;
	private String diagnosisType;
	private String diagnosisVerificationStatus;
	private LocalDateTime updatedOn;
}
