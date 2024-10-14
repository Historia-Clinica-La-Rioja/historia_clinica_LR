package ar.lamansys.sgh.clinichistory.application.ports;

import ar.lamansys.sgh.clinichistory.domain.completedforms.CompleteParameterBo;
import ar.lamansys.sgh.clinichistory.domain.completedforms.CompletedParameterSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.completedforms.NewCompletedParameterBo;

import java.util.List;
import java.util.Optional;

public interface CompletedParameterizedFormStorage {

	void saveCompletedParameter(Integer completedFormId, NewCompletedParameterBo newCompletedParameterBo);

	Integer saveCompletedParameterizedForm(Long documentId, Integer formId);

	List<CompleteParameterBo> getCompletedFormParameters(Integer id);

	Optional<Integer> getParameterizedFormIdById(Integer id);

}
