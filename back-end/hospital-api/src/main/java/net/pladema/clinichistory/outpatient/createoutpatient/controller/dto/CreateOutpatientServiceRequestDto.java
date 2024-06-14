package net.pladema.clinichistory.outpatient.createoutpatient.controller.dto;

import lombok.Getter;
import lombok.Setter;
import net.pladema.clinichistory.requests.service.domain.EDiagnosticReportStatus;
import net.pladema.clinichistory.requests.servicerequests.controller.dto.observations.AddDiagnosticReportObservationsCommandDto;

import javax.annotation.Nullable;

import java.util.Optional;

@Getter
@Setter
public class CreateOutpatientServiceRequestDto {
	//Same values as EDiagnosticReportStatus
	public enum CreationStatus {
		REGISTERED,
		FINAL;

		public Boolean isFinal() {
			return this.equals(FINAL);
		}
	}
	private Integer healthConditionId;
	private String categoryId;
	private CreationStatus creationStatus;
	@Nullable
	private AddDiagnosticReportObservationsCommandDto observations;
}
