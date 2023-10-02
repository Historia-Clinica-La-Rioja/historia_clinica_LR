package ar.lamansys.refcounterref.infraestructure.output.repository.reference;

import ar.lamansys.refcounterref.application.port.ReferenceCounterReferenceFileStorage;
import ar.lamansys.refcounterref.application.port.ReferenceHealthConditionStorage;
import ar.lamansys.refcounterref.application.port.ReferenceStorage;
import ar.lamansys.refcounterref.application.port.ReferenceStudyStorage;
import ar.lamansys.refcounterref.domain.enums.EReferenceCounterReferenceType;
import ar.lamansys.refcounterref.domain.reference.CompleteReferenceBo;
import ar.lamansys.refcounterref.domain.reference.ReferenceDataBo;
import ar.lamansys.refcounterref.domain.reference.ReferenceSummaryBo;
import ar.lamansys.refcounterref.domain.referenceproblem.ReferenceProblemBo;
import ar.lamansys.refcounterref.domain.snomed.SnomedBo;
import ar.lamansys.refcounterref.infraestructure.output.repository.referencehealthcondition.ReferenceHealthConditionRepository;
import ar.lamansys.refcounterref.infraestructure.output.repository.referencenote.ReferenceNote;
import ar.lamansys.refcounterref.infraestructure.output.repository.referencenote.ReferenceNoteRepository;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPersonPort;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReferenceStorageImpl implements ReferenceStorage {

    private final ReferenceRepository referenceRepository;

    private final ReferenceNoteRepository referenceNoteRepository;

    private final ReferenceHealthConditionRepository referenceHealthConditionRepository;

    private final ReferenceHealthConditionStorage referenceHealthConditionStorage;

    private final ReferenceCounterReferenceFileStorage referenceCounterReferenceFileStorage;

	private final ReferenceStudyStorage referenceStudyStorage;

	private final FeatureFlagsService featureFlagsService;

	private final SharedPersonPort sharedPersonPort;

    @Override
	@Transactional
    public List<Integer> save(List<CompleteReferenceBo> referenceBoList) {
        log.debug("Input parameters -> referenceBoList {}", referenceBoList);
		List<Integer> orderIds = new ArrayList<>();
        referenceBoList.forEach(referenceBo -> {
            Reference ref = new Reference(referenceBo);
            if (referenceBo.getNote() != null) {
                Integer referenceNoteId = referenceNoteRepository.save(new ReferenceNote(referenceBo.getNote())).getId();
                ref.setReferenceNoteId(referenceNoteId);
            }
            Reference reference = referenceRepository.save(ref);
            Integer referenceId = reference.getId();
			List<Integer> referenceHealthConditionIds = referenceHealthConditionStorage.saveProblems(referenceId, referenceBo);
			log.debug("referenceHealthConditionIds, referenceId -> {} {}", referenceHealthConditionIds, referenceId);
			if (referenceBo.getStudy() != null) {
				Integer orderId = referenceStudyStorage.save(referenceBo);
				reference.setServiceRequestId(orderId);
				referenceRepository.save(reference);
				orderIds.add(orderId);
				log.debug("orderId, referenceId -> {} {}", orderId, referenceId);
			}
            referenceCounterReferenceFileStorage.updateReferenceCounterReferenceId(referenceId, referenceBo.getFileIds());
        });
		return orderIds;
    }

    @Override
    public List<ReferenceDataBo> getReferences(Integer patientId, List<Integer> clinicalSpecialtyIds) {
		List<ReferenceDataBo> queryResult = referenceRepository.getReferencesFromOutpatientConsultation(patientId, clinicalSpecialtyIds);
       	queryResult.addAll(referenceRepository.getReferencesFromOdontologyConsultation(patientId, clinicalSpecialtyIds));
		return setReferenceDetails(queryResult);
    }

    @Override
    public List<ReferenceProblemBo> getReferencesProblems(Integer patientId) {
        log.debug("Input parameters -> patientId {} ", patientId);
        return referenceHealthConditionRepository.getReferencesProblemsByPatientId(patientId);
    }

	@Override
	public List<ReferenceSummaryBo> getReferencesSummary(Integer patientId, Integer clinicalSpecialtyId, Integer careLineId, Integer practiceId) {
    	log.debug("Input parameters -> patientId {}, clinicalSpecialtyid {}, careLineId {}, practiceId {} ", patientId, clinicalSpecialtyId, careLineId, practiceId);
		List<ReferenceSummaryBo> queryResult = getReferencesSummaryBySearchCriteria(patientId, clinicalSpecialtyId, careLineId, practiceId);
		boolean featureFlagNameSelfDetermination = featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS);
		queryResult.stream().forEach(r -> r.setIncludeNameSelfDetermination(featureFlagNameSelfDetermination));
		log.debug("Output -> references {} ", queryResult);
		return queryResult;
	}

	private List<ReferenceSummaryBo> getReferencesSummaryBySearchCriteria(Integer patientId, Integer clinicalSpecialtyId, Integer careLineId, Integer practiceId) {
		if (clinicalSpecialtyId != null && practiceId == null)
			return getReferencesSummaryByClinicalSpecialtyId(patientId, clinicalSpecialtyId, careLineId);

		if (clinicalSpecialtyId == null && practiceId != null)
			return  getReferencesSummaryByPracticeId(patientId, practiceId, careLineId);

		return getReferencesSummaryByClinicalSpecialtyIdAndPracticeId(patientId, clinicalSpecialtyId, practiceId, careLineId);
	}

	private List<ReferenceSummaryBo> getReferencesSummaryByClinicalSpecialtyId(Integer patientId, Integer clinicalSpecialtyId, Integer careLineId) {
		List<ReferenceSummaryBo> queryResult = referenceRepository.getReferencesSummaryFromOutpatientConsultationByClinicalSpecialtyId(patientId, clinicalSpecialtyId, careLineId);
		queryResult.addAll(referenceRepository.getReferencesSummaryFromOdontologyConsultationByClinicalSpecialtyId(patientId, clinicalSpecialtyId, careLineId));
		return queryResult;
	}

	private List<ReferenceSummaryBo> getReferencesSummaryByPracticeId(Integer patientId, Integer practiceId, Integer careLineId) {
		List<ReferenceSummaryBo> queryResult = referenceRepository.getReferencesSummaryFromOutpatientConsultationByPracticeId(patientId, practiceId, careLineId);
		queryResult.addAll(referenceRepository.getReferencesSummaryFromOdontologyConsultationByPracticeId(patientId, practiceId, careLineId));
		return queryResult;
	}

	private List<ReferenceSummaryBo> getReferencesSummaryByClinicalSpecialtyIdAndPracticeId(Integer patientId, Integer clinicalSpecialtyId, Integer practiceId, Integer careLineId) {
		List<ReferenceSummaryBo> queryResult = referenceRepository.getReferencesSummaryFromOutpatientConsultationByClinicalSpecialtyIdAndPracticeId(patientId, clinicalSpecialtyId, careLineId, practiceId);
		queryResult.addAll(referenceRepository.getReferencesSummaryFromOdontologyConsultationByClinicalSpecialtyIdAndPracticeId(patientId, clinicalSpecialtyId, careLineId, practiceId));
		return queryResult;
	}
	
	private List<ReferenceDataBo> setReferenceDetails(List<ReferenceDataBo> references) {
		List<Integer> referenceIds = references.stream().map(ReferenceDataBo::getId).collect(Collectors.toList());
		var referencesProblems = referenceHealthConditionRepository.getReferencesProblems(referenceIds);
		var files = referenceCounterReferenceFileStorage.getFilesByReferenceCounterReferenceIdsAndType(referenceIds, EReferenceCounterReferenceType.REFERENCIA);
		var referencesStudiesIds = references.stream().filter(r -> r.getServiceRequestId() != null).collect(Collectors.toMap(ReferenceDataBo::getServiceRequestId, ReferenceDataBo::getId));
		Map<Integer, SnomedBo> referencesProcedures = referenceStudyStorage.getReferencesProcedures(referencesStudiesIds);
		return references.stream()
				.peek(ref -> {
					ref.setProblems(referencesProblems.stream()
							.filter(rp -> rp.getReferenceId().equals(ref.getId()))
							.map(rp -> rp.getSnomed().getPt()).collect(Collectors.toList()));
					ref.setFiles(files.get(ref.getId()));
					ref.setProcedure(referencesProcedures.get(ref.getId()));
					ref.setProfessionalFullName(sharedPersonPort.getCompletePersonNameById(ref.getProfessionalPersonId()));
				}).collect(Collectors.toList());
	}

}
