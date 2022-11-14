package ar.lamansys.refcounterref.infraestructure.output.repository.reference;

import ar.lamansys.refcounterref.application.port.ReferenceCounterReferenceFileStorage;
import ar.lamansys.refcounterref.application.port.ReferenceStorage;
import ar.lamansys.refcounterref.domain.enums.EReferenceCounterReferenceType;
import ar.lamansys.refcounterref.domain.file.ReferenceCounterReferenceFileBo;
import ar.lamansys.refcounterref.domain.reference.ReferenceBo;
import ar.lamansys.refcounterref.domain.reference.ReferenceGetBo;
import ar.lamansys.refcounterref.domain.reference.ReferenceSummaryBo;
import ar.lamansys.refcounterref.domain.referenceproblem.ReferenceProblemBo;
import ar.lamansys.refcounterref.infraestructure.output.repository.reference.Reference;
import ar.lamansys.refcounterref.infraestructure.output.repository.reference.ReferenceRepository;
import ar.lamansys.refcounterref.infraestructure.output.repository.referencehealthcondition.ReferenceHealthCondition;
import ar.lamansys.refcounterref.infraestructure.output.repository.referencehealthcondition.ReferenceHealthConditionPk;
import ar.lamansys.refcounterref.infraestructure.output.repository.referencehealthcondition.ReferenceHealthConditionRepository;
import ar.lamansys.refcounterref.infraestructure.output.repository.referencenote.ReferenceNote;
import ar.lamansys.refcounterref.infraestructure.output.repository.referencenote.ReferenceNoteRepository;
import ar.lamansys.sgh.clinichistory.application.healthCondition.HealthConditionStorage;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedDiaryCareLinePort;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final HealthConditionStorage healthConditionStorage;
    private final ReferenceCounterReferenceFileStorage referenceCounterReferenceFileStorage;
	private final SharedDiaryCareLinePort sharedDiaryCareLinePort;
	private final FeatureFlagsService featureFlagsService;

    @Override
	@Transactional
    public void save(List<ReferenceBo> referenceBoList) {
        log.debug("Input parameters -> referenceBoList {}", referenceBoList);
        referenceBoList.stream().forEach(referenceBo -> {
            Reference ref = new Reference(referenceBo);
            if (referenceBo.getNote() != null) {
                Integer referenceNoteId = referenceNoteRepository.save(new ReferenceNote(referenceBo.getNote())).getId();
                ref.setReferenceNoteId(referenceNoteId);
            }
            Integer referenceId = referenceRepository.save(ref).getId();
            List<ReferenceHealthCondition> referenceHealthConditionList = saveProblems(referenceId, referenceBo);
            log.debug("referenceHealthConditionList, referenceId -> {} {}", referenceHealthConditionList, referenceId);
            referenceCounterReferenceFileStorage.updateReferenceCounterReferenceId(referenceId, referenceBo.getFileIds());
        });
    }

    @Override
    public List<ReferenceGetBo> getReferences(Integer patientId, List<Integer> clinicalSpecialtyIds) {
        List<ReferenceGetBo> queryResult = referenceRepository.getReferencesFromOutpatientConsultation(patientId, clinicalSpecialtyIds);
        queryResult.addAll(referenceRepository.getReferencesFromOdontologyConsultation(patientId, clinicalSpecialtyIds));

        List<Integer> referenceIds = queryResult.stream().map(ReferenceGetBo::getId).collect(Collectors.toList());

        Map<Integer, List<ReferenceProblemBo>> problems = referenceHealthConditionRepository.getReferencesProblems(referenceIds)
                .stream()
                .map(ReferenceProblemBo::new)
                .collect(Collectors.groupingBy(ReferenceProblemBo::getReferenceId));

        queryResult = queryResult.stream().map(ref -> {
            ref.setProblems(problems.get(ref.getId()));
            return ref;
        }).collect(Collectors.toList());

        Map<Integer, List<ReferenceCounterReferenceFileBo>> files = referenceCounterReferenceFileStorage.getFilesByReferenceCounterReferenceIdsAndType(referenceIds, EReferenceCounterReferenceType.REFERENCIA);
        List<ReferenceGetBo> result = queryResult.stream().map(ref -> {
            ref.setFiles(files.get(ref.getId()));
            return ref;
        }).collect(Collectors.toList());

        return result;
    }

    @Override
    public List<ReferenceProblemBo> getReferencesProblems(Integer patientId) {
        log.debug("Input parameters -> patientId {} ", patientId);
        return referenceHealthConditionRepository.getReferencesProblemsByPatientId(patientId);
    }

	@Override
	public List<ReferenceSummaryBo> getReferencesSummary(Integer patientId, Integer clinicalSpecialtyId, Integer diaryId) {
    	log.debug("Input parameters -> patientId {}, clinicalSpecialtyid {}, diaryId {} ", patientId, clinicalSpecialtyId, diaryId);
		List<ReferenceSummaryBo> queryResult = referenceRepository.getReferencesSummaryFromOutpatientConsultation(patientId, clinicalSpecialtyId);
		queryResult.addAll(referenceRepository.getReferencesSummaryFromOdontologyConsultation(patientId, clinicalSpecialtyId));
		List<Integer> careLinesOfDiary = sharedDiaryCareLinePort.getCareLineIdsByDiaryId(diaryId);
		if (!careLinesOfDiary.isEmpty())
			queryResult = queryResult.stream().filter(r -> r.getCareLineId() == null || careLinesOfDiary.contains(r.getCareLineId())).collect(Collectors.toList());
		boolean featureFlagNameSelfDetermination = featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS);
		queryResult.stream().forEach(r -> r.setIncludeNameSelfDetermination(featureFlagNameSelfDetermination));
		log.debug("Output -> references {} ", queryResult);
		return queryResult;
	}

	public List<ReferenceHealthCondition> saveProblems(Integer referenceId, ReferenceBo referenceBo) {
        return referenceBo.getProblems().stream().map(problem -> {
            Integer healthConditionId = healthConditionStorage.getHealthConditionIdByEncounterAndSnomedConcept(
                    referenceBo.getEncounterId(), referenceBo.getSourceTypeId(), problem.getSnomed().getSctid(), problem.getSnomed().getPt());
            ReferenceHealthConditionPk refPk = new ReferenceHealthConditionPk(referenceId, healthConditionId);
            return referenceHealthConditionRepository.save(new ReferenceHealthCondition(refPk));
        }).collect(Collectors.toList());
    }

}
