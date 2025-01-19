package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.completedforms;

import ar.lamansys.sgh.clinichistory.domain.completedforms.CompleteParameterBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.completedforms.entity.CompletedParameter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CompletedParameterRepository extends JpaRepository<CompletedParameter, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT NEW ar.lamansys.sgh.clinichistory.domain.completedforms.CompleteParameterBo(" +
			"cp.parameterId, cp.parameterTextOptionId, cp.numericValue, cp.textValue, s.pt, s.sctid) " +
			"FROM CompletedParameter cp " +
			"LEFT JOIN Snomed s ON (s.id = cp.snomedId) " +
			"WHERE cp.completedParameterizedFormId = :completedParameterizedFormId")
	List<CompleteParameterBo> getByCompletedParameterizedFormId(@Param("completedParameterizedFormId") Integer completedParameterizedFormId);
}
