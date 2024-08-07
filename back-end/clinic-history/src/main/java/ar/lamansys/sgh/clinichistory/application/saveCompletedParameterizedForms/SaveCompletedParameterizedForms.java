package ar.lamansys.sgh.clinichistory.application.saveCompletedParameterizedForms;

import ar.lamansys.sgh.clinichistory.application.ports.CompletedParameterizedFormStorage;
import ar.lamansys.sgh.clinichistory.application.saveCompletedParameterizedForms.exceptions.SaveCompletedParameterizedFormsException;
import ar.lamansys.sgh.clinichistory.application.saveCompletedParameterizedForms.exceptions.SaveCompletedParameterizedFormsExceptionEnum;
import ar.lamansys.sgh.clinichistory.domain.completedforms.CompleteParameterizedFormBo;
import ar.lamansys.sgh.clinichistory.domain.completedforms.CompleteParameterBo;
import ar.lamansys.sgh.clinichistory.domain.completedforms.NewCompletedParameterBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.domain.ips.services.SnomedService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class SaveCompletedParameterizedForms {

	private final CompletedParameterizedFormStorage completedParameterizedFormStorage;
	private final SnomedService snomedService;

	@Transactional
	public List<Integer> run(Long documentId, List<CompleteParameterizedFormBo> completedForms){
		log.debug("Input parameters -> completedForms {}", completedForms);
		List<Integer> result = new ArrayList<>();
			if (documentId != null && completedForms != null && !completedForms.isEmpty()) {
				completedForms.forEach(completedFormBo -> {
					Integer completedFormId = completedParameterizedFormStorage.saveCompletedParameterizedForm(documentId, completedFormBo.getId());
					for(CompleteParameterBo parameterBo: completedFormBo.getParameters()) {
						assertValidParameter(parameterBo);
						completedParameterizedFormStorage.saveCompletedParameter(completedFormId, buildParameter(parameterBo));
					}
					result.add(completedFormId);
				});
			}
		log.debug("Output -> result {}", result);
		return result;
	}

	private NewCompletedParameterBo buildParameter(CompleteParameterBo completeParameterBo){
		NewCompletedParameterBo result = new NewCompletedParameterBo();
		result.setParameterId(completeParameterBo.getId());
		result.setOptionId(completeParameterBo.getOptionId());
		result.setTextValue(completeParameterBo.getTextValue());
		result.setNumericValue(completeParameterBo.getNumericValue());
		if (completeParameterBo.isSnomed()){
			SnomedBo snomedBo = new SnomedBo(completeParameterBo.getConceptSctid(), completeParameterBo.getConceptPt());
			Integer snomedId = snomedService.getSnomedId(snomedBo).orElse(snomedService.createSnomedTerm(snomedBo));
			result.setSnomedId(snomedId);
		}
		return result;
	}

	private void assertValidParameter(CompleteParameterBo completeParameterBo){
		if (completeParameterBo.getId() == null)
			throw new SaveCompletedParameterizedFormsException(SaveCompletedParameterizedFormsExceptionEnum.INVALID_PARAMETER_ID, "Existen parámetros sin id");
		if (!completeParameterBo.hasValues())
			throw new SaveCompletedParameterizedFormsException(SaveCompletedParameterizedFormsExceptionEnum.INVALID_PARAMETER_FIELDS, "Uno o más parámetros presentan errores");
	}

}
