package ar.lamansys.sgh.shared.infrastructure.input.service.servicerequest.dto;

import lombok.Getter;
import lombok.Setter;
import ar.lamansys.sgh.shared.infrastructure.input.service.servicerequest.dto.AddDiagnosticReportObservationsCommandDto;

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

	/**
	 * See CreateOutpatientConsultationServiceRequestImpl#findHealthCondition to understand the health condition
	 * params.
	 */
	private String healthConditionSctid;
	private String healthConditionPt;
	private String categoryId;
	private CreationStatus creationStatus;
	@Nullable
	private AddDiagnosticReportObservationsCommandDto observations;
}
