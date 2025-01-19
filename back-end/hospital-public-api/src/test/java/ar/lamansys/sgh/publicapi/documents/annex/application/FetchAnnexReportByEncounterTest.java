package ar.lamansys.sgh.publicapi.documents.annex.application;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import ar.lamansys.sgh.shared.domain.annex.SharedAppointmentAnnexPdfReportResponse;

import ar.lamansys.sgx.shared.filestorage.application.FileContentBo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.lamansys.sgh.publicapi.documents.annex.application.exception.FetchAnnexReportByEncounterAccessDeniedException;
import ar.lamansys.sgh.publicapi.documents.annex.application.exception.FetchAnnexReportByEncounterException;
import ar.lamansys.sgh.shared.application.annex.SharedAppointmentAnnexPdfReportService;
import ar.lamansys.sgh.shared.application.encounter.SharedEncounterToAppointmentService;

@ExtendWith(MockitoExtension.class)
public class FetchAnnexReportByEncounterTest {

	private FetchAnnexReportByEncounter fetchAnnexReportByEncounter;

	@Mock
	private SharedAppointmentAnnexPdfReportService sharedReportAdapter;

	@Mock
	private SharedEncounterToAppointmentService sharedEncounterToAppointmentService;

	@Mock
	private AnnexReportByEncounterPublicApiPermissionsPort annexReportByEncounterPublicApiPermissionsPort;

	private SharedAppointmentAnnexPdfReportResponse successResponse;

	@BeforeEach
	void setup() {
		fetchAnnexReportByEncounter = new FetchAnnexReportByEncounter(
				sharedReportAdapter,
				sharedEncounterToAppointmentService,
				annexReportByEncounterPublicApiPermissionsPort
		);

		successResponse = new SharedAppointmentAnnexPdfReportResponse() {
			@Override
			public FileContentBo getPdf() {
				return null;
			}

			@Override
			public String getFilename() {
				return "success";
			}
		};
	}

	@Test
	public void user_doesnt_has_permissions_on_institution(){
		Integer encounterId = 1;
		Short sourceTypeId = 1;
		String scope = "AMBULATORIA";
		String refsetCode = "refset";
		Integer institutionId = 1;

		when(annexReportByEncounterPublicApiPermissionsPort.canAccess()).thenReturn(false);

		assertThrows(
				FetchAnnexReportByEncounterAccessDeniedException.class,
				() ->fetchAnnexReportByEncounter.run(refsetCode,encounterId,scope)
		);
	}

	@Test
	public void encounter_id_doesnt_exist() {
		Integer encounterId = 1;
		Integer appointmentId = 2;
		Short sourceTypeId = 1;
		String scope = "AMBULATORIA";
		String refsetCode = "refset";
		Integer institutionId = 1;

		when(annexReportByEncounterPublicApiPermissionsPort.canAccess()).thenReturn(true);

		when(sharedEncounterToAppointmentService.run(encounterId, sourceTypeId))
				.thenReturn(Optional.empty());
		when(sharedEncounterToAppointmentService.sourceTypeSupported(any())).thenReturn(true);

		assertThrows(
			FetchAnnexReportByEncounterException.class,
				() ->fetchAnnexReportByEncounter.run(refsetCode,encounterId,scope)
		);
	}

	@Test
	public void scope_is_not_supported(){
		Integer encounterId = 1;
		Short sourceTypeId = 1;
		String invalidScope = "INVALID";
		String validScope = "AMBULATORIA";
		String refsetCode = "refset";
		Integer institutionId = 1;

		/**
		 * Case 1: invalid scope
		 */

		when(annexReportByEncounterPublicApiPermissionsPort.canAccess()).thenReturn(true);
		when(sharedEncounterToAppointmentService.sourceTypeSupported(any())).thenReturn(true);

		assertThrows(
				FetchAnnexReportByEncounterException.class,
				() ->fetchAnnexReportByEncounter.run(refsetCode, encounterId, invalidScope)
		);


		/**
		* Case 2: valid scope but corresponding source type not supported
		*/
		when(sharedEncounterToAppointmentService.sourceTypeSupported(any())).thenReturn(false);

		assertThrows(
			FetchAnnexReportByEncounterException.class,
			() ->fetchAnnexReportByEncounter.run(refsetCode, encounterId, validScope)
		);
	}

	@Test
	public void report_generation_fails_and_exception_is_translated(){
		Integer encounterId = 1;
		Integer appointmentId = 2;
		Short sourceTypeId = 1;
		String scope = "AMBULATORIA";
		String refsetCode = "refset";
		Integer institutionId = 1;

		when(annexReportByEncounterPublicApiPermissionsPort.canAccess()).thenReturn(true);

		when(sharedEncounterToAppointmentService.run(encounterId, sourceTypeId))
				.thenReturn(Optional.of(appointmentId));
		when(sharedEncounterToAppointmentService.sourceTypeSupported(any())).thenReturn(true);

		when(sharedReportAdapter.run(appointmentId)).
		thenThrow(new RuntimeException());

		assertThrows(
				FetchAnnexReportByEncounterException.class,
				() ->fetchAnnexReportByEncounter.run(refsetCode, encounterId, scope)
		);
	}

	@Test
	public void report_is_generated_correctly(){
		Integer encounterId = 1;
		Integer appointmentId = 2;
		Short sourceTypeId = 1;
		String scope = "AMBULATORIA";
		String refsetCode = "refset";
		Integer institutionId = 1;

		when(annexReportByEncounterPublicApiPermissionsPort.canAccess()).thenReturn(true);

		when(sharedEncounterToAppointmentService.run(encounterId, sourceTypeId))
				.thenReturn(Optional.of(appointmentId));
		when(sharedEncounterToAppointmentService.sourceTypeSupported(any())).thenReturn(true);

		when(sharedReportAdapter.run(appointmentId)).thenReturn(successResponse);

		var response = assertDoesNotThrow(() -> fetchAnnexReportByEncounter.run(refsetCode, encounterId, scope));

		assertEquals(response.getFilename(), successResponse.getFilename());

	}
}
