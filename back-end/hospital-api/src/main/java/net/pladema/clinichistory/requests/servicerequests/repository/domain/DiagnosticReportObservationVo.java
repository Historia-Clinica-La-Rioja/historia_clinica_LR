package net.pladema.clinichistory.requests.servicerequests.repository.domain;

import lombok.Getter;
import net.pladema.procedure.domain.ProcedureParameterTypeBo;

import java.util.Objects;

@Getter
public class DiagnosticReportObservationVo {

	//DiagnosticReportObservationGroup fields
	private Integer diagnosticReportObservationGroupId;

	private Integer diagnosticReportId;

	private Integer procedureTemplateId;

	private Boolean isPartialUpload;

	//DiagnosticReportObservation fields
	private Integer diagnosticReportObservationId;

	private Integer procedureParameterId;

	private String value;

	private Short unitOfMeasureId;

	//Extra fields needed to show the observation user-friendly representation
	String loincCodeDescription;
	String loincCodeDisplayName;
	String loincCodeCustomDisplayName;
	Short procedureParameterTypeId;
	String unitOfMeasureDescription;
	String snomedSctid;
	String snomedPt;

	/**
	 * Snomed values are set separately
	 */
	public DiagnosticReportObservationVo(Integer diagnosticReportObservationGroupId, Integer diagnosticReportId, Integer procedureTemplateId,
		Boolean isPartialUpload, Integer diagnosticReportObservationId, Integer procedureParameterId, String value,
		Short unitOfMeasureId, String loincCodeDescription, String loincCodeDisplayName, String loincCodeCustomDisplayName,
		Short procedureParameterTypeId, String unitOfMeasureDescription) {
		this.diagnosticReportObservationGroupId = diagnosticReportObservationGroupId;
		this.diagnosticReportId = diagnosticReportId;
		this.procedureTemplateId = procedureTemplateId;
		this.isPartialUpload = isPartialUpload;
		this.diagnosticReportObservationId = diagnosticReportObservationId;
		this.procedureParameterId = procedureParameterId;
		this.value = value;
		this.unitOfMeasureId = unitOfMeasureId;
		this.loincCodeDescription = loincCodeDescription;
		this.loincCodeDisplayName = loincCodeDisplayName;
		this.loincCodeCustomDisplayName = loincCodeCustomDisplayName;
		this.procedureParameterTypeId = procedureParameterTypeId;
		this.unitOfMeasureDescription = unitOfMeasureDescription;
	}

	public boolean isSnomed() {
		return Objects.equals(this.procedureParameterTypeId, ProcedureParameterTypeBo.SNOMED_ECL);
	}

	public void setSnomedValues(String snomedSctid, String snomedPt) {
		this.snomedSctid = snomedSctid;
		this.snomedPt = snomedPt;
	}
}
