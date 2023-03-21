package ar.lamansys.refcounterref.application.port;

import ar.lamansys.refcounterref.domain.reference.ReferenceBo;
import ar.lamansys.refcounterref.domain.reference.ReferenceGetBo;
import ar.lamansys.refcounterref.domain.reference.ReferenceSummaryBo;
import ar.lamansys.refcounterref.domain.referenceproblem.ReferenceProblemBo;

import java.util.List;

public interface ReferenceStorage {

    void save(List<ReferenceBo> referenceBoList);

    List<ReferenceGetBo> getReferences(Integer patientId, List<Integer> clinicalSpecialtyIds);

    List<ReferenceProblemBo> getReferencesProblems(Integer patientId);

    List<ReferenceSummaryBo> getReferencesSummary(Integer patientId, Integer clinicalSpecialtyId, Integer diaryId);
}
