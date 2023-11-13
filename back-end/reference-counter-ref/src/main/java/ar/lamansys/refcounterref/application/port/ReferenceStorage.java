package ar.lamansys.refcounterref.application.port;

import ar.lamansys.refcounterref.domain.reference.CompleteReferenceBo;
import ar.lamansys.refcounterref.domain.reference.ReferenceDataBo;
import ar.lamansys.refcounterref.domain.reference.ReferenceRequestBo;
import ar.lamansys.refcounterref.domain.reference.ReferenceSummaryBo;
import ar.lamansys.refcounterref.domain.referenceproblem.ReferenceProblemBo;
import ar.lamansys.refcounterref.infraestructure.output.repository.reference.Reference;

import java.util.List;
import java.util.Optional;

public interface ReferenceStorage {

	List<Integer> save(List<CompleteReferenceBo> referenceBos);

    List<ReferenceDataBo> getReferences(Integer patientId, List<Integer> clinicalSpecialtyIds);

    List<ReferenceProblemBo> getReferencesProblems(Integer patientId);

    List<ReferenceSummaryBo> getReferencesSummary(Integer patientId, Integer clinicalSpecialtyId, Integer careLineId, Integer practiceId);

	Optional<ReferenceDataBo> getReferenceData(Integer referenceId);

    void delete(Integer referenceId);
    
	Optional<ReferenceRequestBo> getReferenceByServiceRequestId(Integer serviceRequestId);

	Optional<Reference> findById(Integer referenceId);

}
