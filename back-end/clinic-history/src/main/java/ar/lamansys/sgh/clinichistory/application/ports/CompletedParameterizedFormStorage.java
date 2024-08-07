package ar.lamansys.sgh.clinichistory.application.ports;

import ar.lamansys.sgh.clinichistory.domain.completedforms.NewCompletedParameterBo;

public interface CompletedParameterizedFormStorage {

	void saveCompletedParameter(Integer completedFormId, NewCompletedParameterBo newCompletedParameterBo);

	Integer saveCompletedParameterizedForm(Long documentId, Integer formId);

}
