package ar.lamansys.sgh.publicapi.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class SingleDiagnosticBo {
	private SnomedBo diagnostic;
	private Boolean isMain;
	private String diagnosisType;
	private String diagnosisVerificationStatus;
	private LocalDateTime updatedOn;
}

