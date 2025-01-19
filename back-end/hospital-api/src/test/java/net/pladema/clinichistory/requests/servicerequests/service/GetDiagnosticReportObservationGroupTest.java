package net.pladema.clinichistory.requests.servicerequests.service;

import ar.lamansys.sgh.clinichistory.application.observation.FetchFhirObservationService;
import ar.lamansys.sgh.clinichistory.domain.ips.FhirObservationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.FhirObservationGroupBo;
import ar.lamansys.sgh.clinichistory.domain.ips.FhirQuantityBo;
import net.pladema.clinichistory.requests.servicerequests.application.GetDiagnosticReportObservationGroup;

import net.pladema.clinichistory.requests.servicerequests.application.port.DiagnosticReportObservationStorage;
import net.pladema.clinichistory.requests.servicerequests.domain.observations.EDiagnosticReportObservationGroupSource;
import net.pladema.loinc.application.FetchLoincCode;

import net.pladema.loinc.application.FetchLoincCodeBo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class GetDiagnosticReportObservationGroupTest {

	private GetDiagnosticReportObservationGroup getDiagnosticReportObservationGroup;

	@Mock
	private DiagnosticReportObservationStorage diagnosticReportObservationStorage;
	@Mock
	private FetchFhirObservationService fetchFhirObservationService;
	@Mock
	private FetchLoincCode fetchLoincCode;

	@BeforeEach
	void setup() {
		getDiagnosticReportObservationGroup = new GetDiagnosticReportObservationGroup(
			diagnosticReportObservationStorage,
			fetchFhirObservationService,
			fetchLoincCode
		);
	}

	@Test
	public void fhir_observation_group_is_mapped_correctly() {

		Integer diagnosticReportId = 1;

		// Force to lookup fhir observations
		when(diagnosticReportObservationStorage
		.findObservationsByDiagnosticReportIds(any()))
		.thenReturn(Collections.emptyMap());

		FhirObservationGroupBo groupWithoutObservations = new FhirObservationGroupBo(
			123, 123, diagnosticReportId, new ArrayList<>()
		);

		when(fetchFhirObservationService.run(diagnosticReportId))
		.thenReturn(Optional.of(groupWithoutObservations));

		var result = getDiagnosticReportObservationGroup.run(diagnosticReportId);

		assertNull(result.getObservations());
		assertEquals(diagnosticReportId, result.getDiagnosticReportId());
		assertEquals(EDiagnosticReportObservationGroupSource.WITHOUT_PROCEDURE_TEMPLATE, result.getSource());
		assertNull(result.getProcedureTemplateId());
	}

	@Test
	public void fhir_observations_are_mapped_correctly() {

		/**
		 *
		 * Setup
		 *
		 */
		Integer diagnosticReportId = 1;

		String loinc1Code = "1230-232";
		String loinc1Desc = "loinc 1";

		String loinc2Code = "4321-321";
		String loinc2Desc = "loinc 2";

		FhirObservationBo obsWithQty = new FhirObservationBo(
			1, 123, loinc1Code , "value obsWithQty",
			new FhirQuantityBo(1, BigDecimal.valueOf(123.1), "mm3")
		);

		FhirObservationBo obsWithoutQty = new FhirObservationBo(
				2, 123, loinc2Code, "value obsWithoutQty",
				null
		);

		FhirObservationGroupBo groupWithoutObservations = new FhirObservationGroupBo(
				123, 123, diagnosticReportId, List.of(obsWithQty, obsWithoutQty)
		);


		// Force to lookup fhir observations
		when(diagnosticReportObservationStorage
				.findObservationsByDiagnosticReportIds(any()))
				.thenReturn(Collections.emptyMap());

		when(fetchFhirObservationService.run(diagnosticReportId))
				.thenReturn(Optional.of(groupWithoutObservations));


		when(fetchLoincCode.findByCodes(any()))
		.thenReturn(
			Map.of(
				loinc1Code, new FetchLoincCodeBo(1, loinc1Desc, loinc1Code, "", ""),
				loinc2Code, new FetchLoincCodeBo(2, loinc2Desc, loinc2Code, "", "")
			)
		);

		/**
		 *
		 * Assert
		 *
		 */
		var result = getDiagnosticReportObservationGroup.run(diagnosticReportId);

		var resultObs0 = result.getObservations().get(0);
		var resultObs1 = result.getObservations().get(1);

		assertEquals(obsWithQty.getValue(), resultObs0.getValue());
		assertEquals(
			obsWithQty.getQuantity().getValue() + " " + obsWithQty.getUnitDescription().get(),
			resultObs0.getRepresentation().getValue()
		);
		assertEquals(
			loinc1Desc, resultObs0.getRepresentation().getDescription()
		);

		assertEquals(
				obsWithoutQty.getValue(),
				resultObs1.getRepresentation().getValue()
		);
		assertEquals(
				loinc2Desc, resultObs1.getRepresentation().getDescription()
		);

	}

}
