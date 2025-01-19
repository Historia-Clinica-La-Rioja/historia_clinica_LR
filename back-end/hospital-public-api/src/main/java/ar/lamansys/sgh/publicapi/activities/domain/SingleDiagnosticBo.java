package ar.lamansys.sgh.publicapi.activities.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class SingleDiagnosticBo {
	private SnomedCIE10Bo diagnostic;
	private Boolean isMain;
	private String diagnosisType;
	private String diagnosisVerificationStatus;
	private LocalDateTime updatedOn;
}

