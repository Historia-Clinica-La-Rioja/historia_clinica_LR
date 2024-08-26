package net.pladema.clinichistory.requests.servicerequests.infrastructure.output;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportTreeBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.DiagnosticReportRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.DiagnosticReportTreeRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.DiagnosticReportStatus;
import net.pladema.clinichistory.requests.servicerequests.domain.observations.GetDiagnosticReportObservationGroupBo;
import net.pladema.clinichistory.requests.servicerequests.domain.observations.exceptions.DiagnosticReportNotFoundException;
import net.pladema.clinichistory.requests.servicerequests.domain.observations.exceptions.DiagnosticReportObservationException;
import net.pladema.clinichistory.requests.servicerequests.domain.observations.exceptions.ObservationGroupNotFoundException;
import net.pladema.clinichistory.requests.servicerequests.domain.observations.exceptions.ObservationNotFoundException;
import net.pladema.clinichistory.requests.servicerequests.domain.observations.exceptions.UnitOfMeasureNotFoundException;
import net.pladema.clinichistory.requests.servicerequests.repository.entity.DiagnosticReportObservation;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.pladema.clinichistory.requests.servicerequests.application.port.DiagnosticReportObservationStorage;
import net.pladema.clinichistory.requests.servicerequests.domain.observations.DiagnosticReportObservationsForUpdateVo;
import net.pladema.clinichistory.requests.servicerequests.domain.observations.NewDiagnosticReportObservationBo;
import net.pladema.clinichistory.requests.servicerequests.domain.observations.UpdatedDiagnosticReportObservationBo;
import net.pladema.clinichistory.requests.servicerequests.repository.DiagnosticReportObservationGroupRepository;
import net.pladema.clinichistory.requests.servicerequests.repository.DiagnosticReportObservationRepository;
import net.pladema.clinichistory.requests.servicerequests.repository.entity.DiagnosticReportObservationGroup;
import net.pladema.unitofmeasure.insfrastructure.output.repository.UnitOfMeasureRepository;

@Service
@RequiredArgsConstructor
public class DiagnosticReportObservationStorageImpl implements DiagnosticReportObservationStorage {
	private final DiagnosticReportObservationRepository diagnosticReportObservationRepository;
	private final DiagnosticReportObservationGroupRepository diagnosticReportObservationGroupRepository;
	private final UnitOfMeasureRepository unitOfMeasureRepository;
	private final DiagnosticReportTreeRepository diagnosticReportTreeRepository;
	private final DiagnosticReportRepository diagnosticReportRepository;

	/**
	 * Build the observation group + observations bundle
	 * to be updated.
	 * Returns empty if there are no observations for the given diagnostic report
	 */
	@Override
	public Optional<DiagnosticReportObservationsForUpdateVo> findObservationsForUpdateByDiagnosticReportId(Integer diagnosticReportId) {

		return diagnosticReportObservationGroupRepository
		.findByDiagnosticReportId(diagnosticReportId)
		.map(group -> {
			var ret =  new DiagnosticReportObservationsForUpdateVo(group.getProcedureTemplateId(), group.getId());

			diagnosticReportObservationRepository
			.findByDiagnosticReportObservationGroupId(group.getId())
			.forEach(obs -> ret.addObservation(obs.getId(), obs.getProcedureParameterId(), obs.getValue(), obs.getUnitOfMeasureId()));
			return ret;
		});
	}

	/**
	*
	* To get the observations of the given drs they must be first mapped to the parent (root) diagnostic report.
	* See DiagnosticReportTree and HSI-6750
	*/
	@Override
	public Map<Integer, GetDiagnosticReportObservationGroupBo> findObservationsByDiagnosticReportIds(List<Integer> diagnosticReportIds) {
		Map<Integer, Integer> rootToChildDiagnosticReport = mapRootToChild(diagnosticReportIds);
		Collection<Integer> rootIds = rootToChildDiagnosticReport.keySet();
		Map<Integer, GetDiagnosticReportObservationGroupBo> ret = new HashMap<>();

		//Query using the root ids
		//The output ret uses the child ids as keys
		diagnosticReportObservationGroupRepository
		.findGroupAndObservationsByDiagnosticReportIds(new ArrayList<>(rootIds))
		.forEach(observation -> {

			var rootId = observation.getDiagnosticReportId();
			var childId = rootToChildDiagnosticReport.get(rootId);

			var diagnosticReportObservations = ret.get(childId);

			if (diagnosticReportObservations == null) {
				diagnosticReportObservations = GetDiagnosticReportObservationGroupBo.withProcedureTemplate(
					observation.getDiagnosticReportObservationGroupId(),
					observation.getDiagnosticReportId(),
					observation.getProcedureTemplateId(),
					observation.getIsPartialUpload()
				);
				ret.put(
					childId,
					diagnosticReportObservations);
			}
			diagnosticReportObservations
				.addObservationWithParameter(
					observation.getDiagnosticReportObservationId(),
					observation.getProcedureParameterId(),
					observation.getValue(),
					observation.getUnitOfMeasureId(),
					observation.getLoincCodeDescription(),
					observation.getLoincCodeDisplayName(),
					observation.getLoincCodeCustomDisplayName(),
					observation.getProcedureParameterTypeId(),
					observation.getUnitOfMeasureDescription(),
					observation.getSnomedSctid(),
					observation.getSnomedPt()
				);
		});
		return ret;
	}

	private Map<Integer, Integer> mapRootToChild(List<Integer> diagnosticReportIds) {
		Map<Integer, Integer> ret = new HashMap<>();
		List<DiagnosticReportTreeBo> childToRoot =  diagnosticReportTreeRepository.findRoots(diagnosticReportIds);
		diagnosticReportIds.stream().forEach(drId -> {
			//Find the drId's parent root.
			var rootId = childToRoot.stream()
			.filter(ctr -> ctr.getDiagnosticReportChildId().equals(drId))
			.findFirst()
			.map(ctr -> ctr.getDiagnosticReportParentId())
			.orElse(drId);
			ret.put(rootId, drId);
		});
		return ret;
	}

	@Override
	public void saveNewObservation(Integer groupId, NewDiagnosticReportObservationBo dro) throws DiagnosticReportObservationException {
		diagnosticReportObservationGroupRepository.findById(groupId).orElseThrow(groupNotFound(groupId));
		DiagnosticReportObservation newObs;
		if (dro.getUnitOfMeasureId().isPresent()) {
			Short uomId = dro.getUnitOfMeasureId().get();
			unitOfMeasureRepository.findById(uomId).orElseThrow(uomNotFound(uomId));
			newObs = DiagnosticReportObservation.newNumericObservation(groupId, dro.getProcedureParameterId(), dro.getValue(), uomId);
		}
		else {
			newObs = DiagnosticReportObservation.newNonNumericObservation(groupId, dro.getProcedureParameterId(), dro.getValue());
		}
		diagnosticReportObservationRepository.save(newObs);
	}

	@Override
	public Integer saveNewObservationGroup(Integer diagnosticReportId, Integer procedureTemplateId, Boolean isPartialUpload) {
		return diagnosticReportObservationGroupRepository
		.findByDiagnosticReportIdAndProcedureTemplateId(diagnosticReportId, procedureTemplateId)
		.map(x -> x.getId())
		.orElseGet(() ->
			diagnosticReportObservationGroupRepository
			.save(new DiagnosticReportObservationGroup(diagnosticReportId, procedureTemplateId, isPartialUpload))
			.getId());
	}

	@Override
	public void updateExistingObservation(UpdatedDiagnosticReportObservationBo updatedObservation) throws DiagnosticReportObservationException {
		var forUpdate = diagnosticReportObservationRepository.findById(updatedObservation.getId()).orElseThrow(observationNotFound(updatedObservation.getId()));
		forUpdate.setValue(updatedObservation.getValue());
		if (updatedObservation.getUnitOfMeasureId().isPresent()) {
			var uomId = updatedObservation.getUnitOfMeasureId().get();
			var uom = unitOfMeasureRepository.findById(uomId).orElseThrow(uomNotFound(uomId));
			forUpdate.setUnitOfMeasureId(uom.getId());
		}
		else {
			forUpdate.setUnitOfMeasureId(null);
		}
		diagnosticReportObservationRepository.save(forUpdate);
	}

	@Override
	public void deleteGroup(Integer groupId) {
		diagnosticReportObservationGroupRepository.deleteById(groupId);
		diagnosticReportObservationRepository.deleteAll(
				diagnosticReportObservationRepository.findByDiagnosticReportObservationGroupId(groupId)
		);
	}

	@Override
	public boolean existsRelatedFinalizedDiagnosticReport(Integer diagnosticReportId) {
		return diagnosticReportObservationGroupRepository
		.findRelatedDiagnosticReports(diagnosticReportId)
		.stream()
		.anyMatch(dr -> dr.getStatusId().equals(DiagnosticReportStatus.FINAL));
	}

	@Override
	public boolean isFinal(Integer diagnosticReportId) throws DiagnosticReportNotFoundException {
		return diagnosticReportRepository
		.findById(diagnosticReportId)
		.map(dr -> dr.isFinal())
		.orElseThrow(() -> new DiagnosticReportNotFoundException(diagnosticReportId));
	}

	@Override
	public void updateDiagnosticReportStatusToPartial(Integer diagnosticReportId) {
		diagnosticReportRepository.updateStatus(diagnosticReportId, DiagnosticReportStatus.PARTIAL);

	}

	@Override
	public boolean existsDiagnosticReport(Integer diagnosticReportId) {
		return diagnosticReportRepository.existsById(diagnosticReportId);
	}

	private Supplier<DiagnosticReportObservationException> uomNotFound(Short uomId) {
		return () -> new UnitOfMeasureNotFoundException(uomId);
	}

	private Supplier<DiagnosticReportObservationException> observationNotFound(Integer observationId) {
		return () -> new ObservationNotFoundException(observationId);
	}

	private Supplier<DiagnosticReportObservationException> groupNotFound(Integer observationGroupId) {
		return () -> new ObservationGroupNotFoundException(observationGroupId);
	}

}
