package ar.lamansys.refcounterref.application.port;

import ar.lamansys.refcounterref.domain.reference.CompleteReferenceBo;
import ar.lamansys.refcounterref.domain.reference.ReferenceDataBo;
import ar.lamansys.refcounterref.domain.reference.ReferenceRequestBo;
import ar.lamansys.refcounterref.domain.reference.ReferenceStudyBo;
import ar.lamansys.refcounterref.domain.reference.ReferenceSummaryBo;
import ar.lamansys.refcounterref.domain.referenceproblem.ReferenceProblemBo;
import ar.lamansys.refcounterref.infraestructure.output.repository.reference.Reference;

import java.util.List;
import java.util.Optional;

public interface ReferenceStorage {

	List<Integer> save(List<CompleteReferenceBo> referenceBos);

    List<ReferenceDataBo> getReferences(Integer patientId, List<Integer> clinicalSpecialtyIds, List<Short> loggedUserRoleIds);

    List<ReferenceProblemBo> getReferencesProblems(Integer patientId, List<Short> loggedUserRoleIds);

    List<ReferenceSummaryBo> getReferencesSummary(Integer patientId, Integer clinicalSpecialtyId, Integer careLineId, Integer practiceId, List<Short> userRoleIds);

	Optional<ReferenceDataBo> getReferenceData(Integer referenceId);

    void delete(Integer referenceId);
    
	Optional<ReferenceRequestBo> getReferenceByServiceRequestId(Integer serviceRequestId);

	Optional<Reference> findById(Integer referenceId);

	Short getReferenceRegulationStateId(Integer referenceId);

	void deleteAndUpdateStatus(Integer referenceId, Short statusId);

	Optional<Integer> getReferenceEncounterTypeId(Integer referenceId);

	Optional<ReferenceStudyBo> getReferenceStudy(Integer referenceId);

	boolean updateDestinationInstitution(Integer referenceId, Integer institutionId);

	Integer getDestinationInstitutionId(Integer referenceId);

	Integer getPatientId(Integer referenceId);
	
	Integer getOriginInstitutionId(Integer referenceId);

}
