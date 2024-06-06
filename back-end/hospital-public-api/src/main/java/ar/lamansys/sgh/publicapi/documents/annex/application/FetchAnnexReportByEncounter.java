package ar.lamansys.sgh.publicapi.documents.annex.application;

import java.util.List;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.documents.annex.application.exception.FetchAnnexReportByEncounterAccessDeniedException;
import ar.lamansys.sgh.publicapi.documents.annex.application.exception.FetchAnnexReportByEncounterException;
import ar.lamansys.sgh.publicapi.domain.ScopeEnum;
import ar.lamansys.sgh.shared.application.annex.SharedAppointmentAnnexPdfReportService;
import ar.lamansys.sgh.shared.application.encounter.SharedEncounterToAppointmentService;
import ar.lamansys.sgh.shared.domain.annex.SharedAppointmentAnnexPdfReportResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FetchAnnexReportByEncounter {

	static final List<String> SUPPORTED_SCOPES = List.of(ScopeEnum.AMBULATORIA.name());
	private final SharedAppointmentAnnexPdfReportService sharedReportAdapter;
	private final SharedEncounterToAppointmentService sharedEncounterToAppointmentService;
	private final AnnexReportByEncounterPublicApiPermissionsPort annexReportByEncounterPublicApiPermissionsPort;

	public SharedAppointmentAnnexPdfReportResponse run(String refsetCode, Integer encounterId, String scope)
		throws FetchAnnexReportByEncounterException
	{
		log.debug("Fetching annex for refsetCode {} encounterId {} scope {}", refsetCode, encounterId, scope);
		assertCanAccessInstitution(refsetCode);
		/**
		 * The scope stored by addons is taken from the activities controller
		 * At the moment (2.20) the only supported scope is AMBULATORIA
		 */
		Short sourceType = scopeToSourceType(scope);

		Integer appointmentId = appointmentId = sharedEncounterToAppointmentService
		.run(encounterId, sourceType)
		.orElseThrow(() -> FetchAnnexReportByEncounterException.appointmentNotFound(refsetCode, encounterId));

		log.debug("Fetching annex report for appointmentId {} sourceType {}", appointmentId, sourceType);

		SharedAppointmentAnnexPdfReportResponse report = null;
		try {
			report = sharedReportAdapter.run(appointmentId);
		}
		catch (Exception e) {
			throw FetchAnnexReportByEncounterException.reportFailed(refsetCode, encounterId, e);
		}
		log.debug("Annex II generated with filename {}", report.getFilename());
		return report;
	}

	private Short scopeToSourceType(String scope) throws FetchAnnexReportByEncounterException {
		if (!SUPPORTED_SCOPES.contains(scope))
			throw FetchAnnexReportByEncounterException.scopeNotSupported(scope);

		Short sourceTypeId = null;
		try {
			sourceTypeId = ScopeEnum.valueOf(scope).getIds().get(0);
		}
		catch (Exception e) {
			throw FetchAnnexReportByEncounterException.scopeNotSupported(scope);
		}

		if (!sharedEncounterToAppointmentService.sourceTypeSupported(sourceTypeId))
			throw FetchAnnexReportByEncounterException.scopeNotSupported(scope);

		return sourceTypeId;
	}

	private void assertCanAccessInstitution(String refsetCode) {
		var canAccess = annexReportByEncounterPublicApiPermissionsPort
				.findInstitutionId(refsetCode)
				.map(annexReportByEncounterPublicApiPermissionsPort::canAccess)
				.orElse(false);
		if (!canAccess)
			throw new FetchAnnexReportByEncounterAccessDeniedException();
	}

}
