package ar.lamansys.odontology.domain.consultation;

import java.util.Optional;

import ar.lamansys.sgh.shared.domain.servicerequest.SharedAddObservationsCommandVo;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceClosureDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class CreateOdontologyConsultationServiceRequestBo {

	@Getter
	@Setter
	@ToString
	@AllArgsConstructor
	public class AddDiagnosticReportObservationCommandBo {

		private Integer procedureParameterId;
		private String value;
		private Short unitOfMeasureId;
		private String snomedSctid;
		private String snomedPt;
	}
	/**
	*
	* Service request data
	*
	* See CreateConsultationServiceRequestImpl#findHealthCondition to understand the health condition
	* params.
	*/
	private String healthConditionSctid;
	private String healthConditionPt;
	private String categoryId;
	private Boolean creationStatusIsFinal;
	private String snomedSctid;
	private String snomedPt;

	/**
	* Observations data
	*/
	private Boolean isPartialUpload;

	private Integer procedureTemplateId;
	private ReferenceClosureDto referenceClosure;

	private SharedAddObservationsCommandVo sharedAddObservationsCommandVo;

	public Optional<SharedAddObservationsCommandVo> getObservations() {
		return Optional.ofNullable(sharedAddObservationsCommandVo);
	}

}
