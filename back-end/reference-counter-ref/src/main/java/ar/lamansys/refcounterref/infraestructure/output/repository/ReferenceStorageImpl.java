package ar.lamansys.refcounterref.infraestructure.output.repository;

import ar.lamansys.refcounterref.application.port.ReferenceStorage;
import ar.lamansys.refcounterref.domain.reference.ReferenceBo;
import ar.lamansys.refcounterref.infraestructure.output.repository.reference.Reference;
import ar.lamansys.refcounterref.infraestructure.output.repository.reference.ReferenceRepository;
import ar.lamansys.refcounterref.infraestructure.output.repository.referencehealthcondition.ReferenceHealthCondition;
import ar.lamansys.refcounterref.infraestructure.output.repository.referencehealthcondition.ReferenceHealthConditionPk;
import ar.lamansys.refcounterref.infraestructure.output.repository.referencehealthcondition.ReferenceHealthConditionRepository;
import ar.lamansys.refcounterref.infraestructure.output.repository.referencenote.ReferenceNote;
import ar.lamansys.refcounterref.infraestructure.output.repository.referencenote.ReferenceNoteRepository;
import ar.lamansys.sgh.clinichistory.application.healthCondition.HealthConditionStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReferenceStorageImpl implements ReferenceStorage {

    private final ReferenceRepository referenceRepository;
    private final ReferenceNoteRepository referenceNoteRepository;
    private final ReferenceHealthConditionRepository referenceHealthConditionRepository;
    private final HealthConditionStorage healthConditionStorage;

    @Override
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
        });
    }

    public List<ReferenceHealthCondition> saveProblems(Integer referenceId, ReferenceBo referenceBo) {
        return referenceBo.getProblems().stream().map(problem -> {
            ReferenceHealthConditionPk refPk = new ReferenceHealthConditionPk();
            if (problem.getId() == null) {
                Integer healthConditionId = healthConditionStorage.getHealthConditionIdByEncounterAndSnomedConcept(
                        referenceBo.getEncounterId(), referenceBo.getSourceTypeId(), problem.getSnomed().getSctid(), problem.getSnomed().getPt());
                refPk.setHealthConditionId(healthConditionId);
            } else {
                refPk.setHealthConditionId(problem.getId());
            }
            refPk.setReferenceId(referenceId);
            return referenceHealthConditionRepository.save(new ReferenceHealthCondition(refPk));
        }).collect(Collectors.toList());
    }

}
