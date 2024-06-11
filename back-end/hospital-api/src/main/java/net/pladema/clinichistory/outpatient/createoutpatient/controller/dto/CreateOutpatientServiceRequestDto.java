package net.pladema.clinichistory.outpatient.createoutpatient.controller.dto;

import lombok.Getter;
import lombok.Setter;
import net.pladema.clinichistory.requests.service.domain.EDiagnosticReportStatus;

import javax.annotation.Nullable;

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
}
