package net.pladema.clinichistory.requests.servicerequests.application;

import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.domain.ips.services.SnomedService;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedReferencePort;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceClosureDto;
import net.pladema.clinichistory.requests.servicerequests.application.port.ProcedureParameterStorage;
import net.pladema.clinichistory.requests.servicerequests.domain.observations.ProcedureParameterBo;
import net.pladema.clinichistory.requests.servicerequests.domain.observations.exceptions.DiagnosticReportFinalizedException;
import net.pladema.clinichistory.requests.servicerequests.domain.observations.exceptions.DiagnosticReportNotFoundException;
import net.pladema.clinichistory.requests.servicerequests.domain.observations.exceptions.DiagnosticReportObservationException;

import net.pladema.clinichistory.requests.servicerequests.domain.observations.UpdatedDiagnosticReportObservationBo;

import net.pladema.clinichistory.requests.servicerequests.domain.observations.exceptions.InvalidProcedureTemplateChangeException;

import net.pladema.clinichistory.requests.servicerequests.domain.observations.exceptions.ParameterTypeAndValueMismatchException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.servicerequests.application.port.DiagnosticReportObservationStorage;
import ar.lamansys.sgh.shared.domain.servicerequest.SharedAddObservationsCommandVo;
import net.pladema.clinichistory.requests.servicerequests.domain.observations.DiagnosticReportObservationsForUpdateVo;
import net.pladema.clinichistory.requests.servicerequests.domain.observations.NewDiagnosticReportObservationBo;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddDiagnosticReportObservations {

	private final DiagnosticReportObservationStorage diagnosticReportObservationStorage;
	private final SnomedService snomedService;
	private final SharedReferencePort sharedReferencePort;
	private final ProcedureParameterStorage procedureParameterStorage;

	/**
	 * The given diagnostic report may have an associated observation group (and it's observations) or not.
	 * If it does, we update the observations. Otherwise, we create a new group and it's observations
	 *
	 */
	@Transactional
	public void run(Integer diagnosticReportId, SharedAddObservationsCommandVo addObservationsCommand, Integer institutionId, Integer patientId) throws DiagnosticReportObservationException, InvalidProcedureTemplateChangeException {
		log.debug("input -> diagnosticRepoortId {}, addObservationsCommand {}", diagnosticReportId, addObservationsCommand);


		/**
		 * If the diagnostic report is linked to a reference it must pass the same validations
		 * as when calling the "complete" method of the service request controller.
		 * See ServiceRequestController::complete.
		 */
		var referenceClosure = addObservationsCommand.getReferenceClosure();
		if (referenceClosure != null) {
			sharedReferencePort.validateReference(mapToReferenceClosureDto(referenceClosure), institutionId, patientId);
		}

		assertDiagnosticReportExists(diagnosticReportId);

		Optional<DiagnosticReportObservationsForUpdateVo> existingObservations =
			diagnosticReportObservationStorage.findObservationsForUpdateByDiagnosticReportId(diagnosticReportId);

		if (existingObservations.isPresent()) {
			updateObservations(diagnosticReportId, existingObservations.get(), addObservationsCommand);
		}
		else {
			createNewObservations(diagnosticReportId, addObservationsCommand);
		}
		updateDiagnosticReportStatus(diagnosticReportId);
	}

	private ReferenceClosureDto mapToReferenceClosureDto (SharedAddObservationsCommandVo.SharedReferenceRequestClosureBo bo){
		ReferenceClosureDto result = new ReferenceClosureDto();
		result.setReferenceId(bo.getReferenceId());
		result.setClosureTypeId(bo.getClosureTypeId());
		result.setCounterReferenceNote(bo.getCounterReferenceNote());
		result.setClinicalSpecialtyId(bo.getClinicalSpecialtyId());
		return result;
	}

	private void assertDiagnosticReportExists(Integer diagnosticReportId) throws DiagnosticReportNotFoundException {
		if (!diagnosticReportObservationStorage.existsDiagnosticReport(diagnosticReportId))
			throw new DiagnosticReportNotFoundException(diagnosticReportId);
	}

	/**
	 * If the diagnostic report's status isn't final it's set to PARTIAL. This
	 * indicates that the dr has partial results linked to it. Later, the 'complete'
	 * endpoint will be called to mark the dr as FINAL. See the 'complete' method of
	 * {@link net.pladema.clinichistory.requests.servicerequests.controller.ServiceRequestController}
	 */
	private void updateDiagnosticReportStatus(Integer diagnosticReportId) throws DiagnosticReportNotFoundException {
		if (!diagnosticReportObservationStorage.isFinal(diagnosticReportId))
			diagnosticReportObservationStorage.updateDiagnosticReportStatusToPartial(diagnosticReportId);
	}

	private void createNewObservations(
		Integer diagnosticReportId,
		SharedAddObservationsCommandVo addObservations
	) throws DiagnosticReportObservationException {
		log.debug("Creating new observations");
		validate(diagnosticReportId, addObservations);

		/**
		 * Attach results to the diagnostic report
		 */
		Integer groupId = diagnosticReportObservationStorage.saveNewObservationGroup(
				diagnosticReportId,
				addObservations.getProcedureTemplateId(),
				addObservations.getIsPartialUpload()
		);

		/**
		 * Save each value
		 */
		for (var valueBo : addObservations.getValues()) {
			NewDiagnosticReportObservationBo newObservation;
			valueBo.translateSnomed(this::translateSnomed);
			if (valueBo.isNumeric())
				newObservation = NewDiagnosticReportObservationBo.buildNumeric(
					valueBo.getProcedureParameterId(),
					valueBo.getValue(),
					valueBo.getUnitOfMeasureId(),
					valueBo.getValueNumeric());
			else {
				newObservation = NewDiagnosticReportObservationBo.buildNonNumeric(valueBo.getProcedureParameterId(), valueBo.getValue());
			}
			diagnosticReportObservationStorage.saveNewObservation(groupId, newObservation);
		}
	}

	/**
	 * If the selected template changed:
	 * 	1 The observation group's template id must be updated
	 * 	2 The group's children (the observations that point to it) must be deleted and new ones created
	 * 	according to the new template's parameters.
	 */
	private void updateObservations(Integer diagnosticReportId, DiagnosticReportObservationsForUpdateVo existingObservations, SharedAddObservationsCommandVo updatedObservations) throws DiagnosticReportObservationException, InvalidProcedureTemplateChangeException {
		log.debug("Updating observations");
		Integer groupId = existingObservations.getDiagnosticReportObservationGroupId();
		validate(diagnosticReportId, updatedObservations);

		//Template changed
		if (templateChanged(existingObservations, updatedObservations)) {
			log.debug("Template changed");
			diagnosticReportObservationStorage.deleteGroup(groupId);
			createNewObservations(diagnosticReportId, updatedObservations);
		}
		//The template stays the same. Just update the observations.
		else {
			log.debug("Template stays the same: update observations");
			updatedObservations.getValues().forEach(observation -> observation.translateSnomed(this::translateSnomed));
			for (UpdatedDiagnosticReportObservationBo x : existingObservations.update(updatedObservations)) {
				diagnosticReportObservationStorage.updateExistingObservation(x);
			}
		}
	}

	private boolean templateChanged(DiagnosticReportObservationsForUpdateVo existingObservations, SharedAddObservationsCommandVo updatedObservations) {
		return !existingObservations.getProcedureTemplateId().equals(updatedObservations.getProcedureTemplateId());
	}

	private void validate(Integer diagnosticReportId, SharedAddObservationsCommandVo addObservations) throws DiagnosticReportObservationException {
		if (diagnosticReportObservationStorage.existsRelatedFinalizedDiagnosticReport(diagnosticReportId)) {
			throw new DiagnosticReportFinalizedException();
		}
		//The value type must match the referenced parameter's type
		Map<Integer, ProcedureParameterBo> procedureParametersById = findProcedureParameters(addObservations.getParameterIds());
		for (var value : addObservations.getValues()) {
			var procedureParameter = procedureParametersById.get(value.getProcedureParameterId());
			if (
				value.hasValue() && (
					(procedureParameter.getIsSnomed() && !value.isSnomed()) ||
					(procedureParameter.getIsNumeric() && !value.isNumeric())
				)
			)
			{
				throw new ParameterTypeAndValueMismatchException();
			}
		}
	}

	private Map<Integer, ProcedureParameterBo> findProcedureParameters(List<Integer> parameterIds) {
		return procedureParameterStorage
			.findProcedureParametersById(parameterIds)
			.stream()
			.collect(Collectors.toMap(ProcedureParameterBo::getId, Function.identity()));
	}

	/**
	 * If the observation contains a snomed sctid and pt, the value stored for
	 * the observation will be and id from the snomed table. This method
	 * makes that translation.
	 */
	private String translateSnomed(String snomedSctid, String snomedPt) {
		var concept = new SnomedBo(snomedSctid, snomedPt);
		var conceptId = snomedService.getSnomedId(concept).orElseGet(() -> snomedService.createSnomedTerm(concept));
		return conceptId.toString();
	}
}
