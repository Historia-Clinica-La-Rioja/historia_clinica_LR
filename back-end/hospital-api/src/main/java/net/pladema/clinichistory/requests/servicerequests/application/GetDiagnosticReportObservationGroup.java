package net.pladema.clinichistory.requests.servicerequests.application;

import ar.lamansys.sgh.clinichistory.application.observation.FetchFhirObservationService;
import ar.lamansys.sgh.clinichistory.domain.ips.FhirObservationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.FhirObservationGroupBo;
import lombok.RequiredArgsConstructor;
import net.pladema.clinichistory.requests.servicerequests.application.port.DiagnosticReportObservationStorage;
import net.pladema.clinichistory.requests.servicerequests.domain.observations.GetDiagnosticReportObservationGroupBo;

import net.pladema.loinc.application.FetchLoincCode;

import net.pladema.loinc.application.FetchLoincCodeBo;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetDiagnosticReportObservationGroup {

	private final DiagnosticReportObservationStorage diagnosticReportObservationStorage;
	private final FetchFhirObservationService fetchFhirObservationService;
	private final FetchLoincCode fetchLoincCode;

	/**
	 * The diagnostic report observations come from two sources:
	 * - Observations uploaded from the webapp are found in the
	 * diagnostic_report_{observation, observation_group} tables. These reference a
	 * procedure template and its parameters.
	 * - Observations uploaded via the fhir api are stored in the fhir_{observation,
	 * observation_group} tables.
	 * @param diagnosticReportId
	 * @return
	 */
	public GetDiagnosticReportObservationGroupBo run(Integer diagnosticReportId) {

		/**
		 * Fetch from diagnostic_report_observation
		 */
		GetDiagnosticReportObservationGroupBo observationGroup = diagnosticReportObservationStorage
		.findObservationsByDiagnosticReportIds(List.of(diagnosticReportId))
		.get(diagnosticReportId);

		if (observationGroup != null)
			return observationGroup;

		/**
		 * Fetch from fhir_observation
		 */
		Optional<FhirObservationGroupBo> fhirObservationGroup = fetchFhirObservationService.run(diagnosticReportId);
		if (fhirObservationGroup.isPresent()) {
			return toGetDiagnosticReportObservationGroupBo(fhirObservationGroup.get());
		}

		return GetDiagnosticReportObservationGroupBo.noResultsFound(diagnosticReportId);

	}

	private GetDiagnosticReportObservationGroupBo toGetDiagnosticReportObservationGroupBo(FhirObservationGroupBo fhirObservationGroupBo) {
		var group = GetDiagnosticReportObservationGroupBo
			.withoutProcedureTemplate(
				fhirObservationGroupBo.getDiagnosticReportId(),
				fhirObservationGroupBo.getId());

		Map<String, FetchLoincCodeBo> loincCodes = getLoincMap(fhirObservationGroupBo.getObservations());

		fhirObservationGroupBo.getObservations().forEach(fhirObs -> {
			var fhirObsLoinc = loincCodes.getOrDefault(fhirObs.getLoincCode(), FetchLoincCodeBo.empty());
			group.addObservationWithoutParameter(
				fhirObs.getId(),
				fhirObs.getValue(),
				fhirObsLoinc.getDescription(),
				fhirObsLoinc.getDisplayName(),
				fhirObsLoinc.getCustomDisplayName(),
				fhirObs.getUnitDescription().orElse(null)
			);
		});
		return group;
	}

	private Map<String, FetchLoincCodeBo> getLoincMap(List<FhirObservationBo> observations) {
		var codes = observations.stream()
		.map(FhirObservationBo::getLoincCode)
		.filter(Objects::nonNull)
		.filter(x -> !x.isEmpty())
		.collect(Collectors.toList());
		return fetchLoincCode.findByCodes(codes);
	}
}
