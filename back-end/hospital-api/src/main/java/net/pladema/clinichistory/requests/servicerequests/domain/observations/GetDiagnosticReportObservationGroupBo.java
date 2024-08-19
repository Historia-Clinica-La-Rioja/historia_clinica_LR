package net.pladema.clinichistory.requests.servicerequests.domain.observations;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;
import net.pladema.procedure.domain.ProcedureParameterTypeBo;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GetDiagnosticReportObservationGroupBo {
	/**
	 * User understandable representation of the observation
	 * Used when showing results of a diagnostic report
	 */
	@Value
	public class Representation {

		String description;
		String value;
	}
	@Value
	public class Observation {

		Integer id;
		Integer procedureParameterId;
		String value;
		Short unitOfMeasureId;
		Representation representation;
		String snomedSctid;
		String snomedPt;
	}
	Integer id;
	Integer diagnosticReportId;
	Integer procedureTemplateId;
	Boolean isPartialUpload;
	List<Observation> observations;
	EDiagnosticReportObservationGroupSource source;

	/**
	 * No observations exist for the given diagnosticReportId
	 * @param diagnosticReportId
	 * @return
	 */
	public static GetDiagnosticReportObservationGroupBo noResultsFound(Integer diagnosticReportId) {
		var observationGroup  = new GetDiagnosticReportObservationGroupBo(
			null,
			diagnosticReportId,
			null,
			false,
			null,
			EDiagnosticReportObservationGroupSource.NOT_FOUND
		);
		return observationGroup;
	}

	/**
	 * The observations found for the given diagnosticReportId were uploaded via the webapp, so they exist in
	 * the {@link net.pladema.clinichistory.requests.servicerequests.repository.entity.DiagnosticReportObservationGroup} entity and its children.
	 */
	public static GetDiagnosticReportObservationGroupBo withProcedureTemplate(
		Integer diagnosticReportObservationGroupId,
		Integer diagnosticReportId,
		Integer procedureTemplateId,
		Boolean isPartialUpload
	)
	{
		var observationGroup = new GetDiagnosticReportObservationGroupBo(
			diagnosticReportObservationGroupId,
			diagnosticReportId,
			procedureTemplateId,
			isPartialUpload,
			null,
			EDiagnosticReportObservationGroupSource.WITH_PROCEDURE_TEMPLATE
		);
		return observationGroup;
	}
	/**
	 * The observations found for the given diagnosticReportId were uploaded via the fhir api, so they exist in
	 * the {@link ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.FhirObservationGroup} entity and its children.
	 */
	public static GetDiagnosticReportObservationGroupBo withoutProcedureTemplate(
		Integer diagnosticReportId,
		Integer fhirDiagnosticReportGroupId
	)
	{
		return new GetDiagnosticReportObservationGroupBo(
			fhirDiagnosticReportGroupId,
			diagnosticReportId,
			null,
			false,
			null,
			EDiagnosticReportObservationGroupSource.WITHOUT_PROCEDURE_TEMPLATE
		);
	}

	/**
	 * Adds an observation that originated from a procedure template and parameters
	 */
	public void addObservationWithParameter(
		Integer diagnosticReportObservationId,
		Integer procedureParameterId,
		String value,
		Short uomId,
		String loincCodeDescription,
		String loincCodeDisplayName,
		String loincCodeCustomDisplayName,
		Short procedureParameterTypeId,
		String unitOfMeasureDescription,
		String snomedSctid,
		String snomedPt)
	{
		if (observations == null) observations = new ArrayList<>();
		observations.add(
			new Observation(
				diagnosticReportObservationId,
				procedureParameterId,
				value,
				uomId,
				buildRepresentationWithParameter(
					value,
					buildLoincCodeDescription(
						loincCodeDescription,
						loincCodeCustomDisplayName,
						loincCodeDisplayName
					),
					procedureParameterTypeId,
					unitOfMeasureDescription,
					snomedSctid,
					snomedPt
				),
				snomedSctid,
				snomedPt
			)
		);
	}

	/**
	 * Adds an observation that came from the fhir api
	 */
	public void addObservationWithoutParameter(
			Integer diagnosticReportObservationId,
			String value,
			String loincCodeDescription,
			String loincCodeDisplayName,
			String loincCodeCustomDisplayName,
			String unitOfMeasureDescription
	)
	{
		if (observations == null) observations = new ArrayList<>();
		observations.add(
				new Observation(
						diagnosticReportObservationId,
						null,
						value,
						null,
						buildRepresentationWithoutParameter(
								value,
								buildLoincCodeDescription(
										loincCodeDescription,
										loincCodeCustomDisplayName,
										loincCodeDisplayName
								),
								unitOfMeasureDescription
						),
						null,
						null
				)
		);
	}

	private String buildLoincCodeDescription(String description, String customDisplayName, String displayName) {
		if (customDisplayName != null && !customDisplayName.isEmpty()) return customDisplayName;
		if (displayName != null && !displayName.isEmpty()) return displayName;
		if (description != null && !description.isEmpty())
			return description;
		return "";
	}

	/**
	 * Sets the user-friendly representation of each observation.
	 */
	private Representation buildRepresentationWithParameter(String value, String loincDescription, Short procedureParameterTypeId, String unitOfMeasureDescription, String snomedSctid, String snomedPt) {
		if (ProcedureParameterTypeBo.isNumeric(procedureParameterTypeId))
			return new Representation(loincDescription, handleNumeric(value, unitOfMeasureDescription));
		else if (ProcedureParameterTypeBo.isFreeText(procedureParameterTypeId))
			return new Representation(loincDescription, handleFreeText(value));
		else if (ProcedureParameterTypeBo.isSnomedEcl(procedureParameterTypeId))
			return new Representation(loincDescription, handleSnomed(snomedSctid, snomedPt));
		else if (ProcedureParameterTypeBo.isTextOption(procedureParameterTypeId))
			return new Representation(loincDescription, handleTextOption(value));
		else return new Representation("", "");
	}

	private Representation buildRepresentationWithoutParameter(String value, String loincDescription, String unitOfMeasureDescription) {
		if (unitOfMeasureDescription != null)
			return new Representation(loincDescription, handleNumeric(value, unitOfMeasureDescription));
		else
			return new Representation(loincDescription, emptyIfNull(value));
	}

	private String handleTextOption(String value) {
		return emptyIfNull(value);
	}

	private String handleSnomed(String snomedSctid, String snomedPt) {
		return emptyIfNull(snomedPt);
	}

	private String handleFreeText(String value) {
		return emptyIfNull(value);
	}

	private static String handleNumeric(String value, String unitOfMeasureDescription) {
		return String.format("%s %s", emptyIfNull(value), emptyIfNull(unitOfMeasureDescription));
	}

	static String emptyIfNull(String value) {
		return value == null ? "" : value;
	}

}
