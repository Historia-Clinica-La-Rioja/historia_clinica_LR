package ar.lamansys.sgh.clinichistory.infrastructure.output;

import ar.lamansys.sgh.clinichistory.application.ports.CompletedParameterizedFormStorage;
import ar.lamansys.sgh.clinichistory.domain.completedforms.CompleteParameterBo;
import ar.lamansys.sgh.clinichistory.domain.completedforms.NewCompletedParameterBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.completedforms.CompletedParameterRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.completedforms.CompletedParameterizedFormRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.completedforms.entity.CompletedParameter;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.completedforms.entity.CompletedParameterizedForm;
import ar.lamansys.sgh.shared.infrastructure.input.service.forms.SharedParameterizedFormPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class CompletedParameterizedFormStorageImpl implements CompletedParameterizedFormStorage {

	private final CompletedParameterizedFormRepository completedParameterizedFormRepository;
	private final CompletedParameterRepository completedParameterRepository;
	private final SharedParameterizedFormPort sharedParameterizedFormPort;

	@Override
	public void saveCompletedParameter(Integer completedFormId, NewCompletedParameterBo newCompletedParameterBo) {
		log.debug("Input parameters -> completeFormId {}, newCompletedParameterBo {}", completedFormId, newCompletedParameterBo);
		completedParameterizedFormRepository.findById(completedFormId).ifPresent(completedParameterizedForm -> {
			completedParameterRepository.save(buildEntity(completedFormId, newCompletedParameterBo));
		});
	}

	@Override
	public Integer saveCompletedParameterizedForm(Long documentId, Integer formId) {
		log.debug("Input parameters -> documentId {}, formId {}", documentId, formId);
		Integer result = completedParameterizedFormRepository.save(new CompletedParameterizedForm(formId, documentId)).getId();
		log.debug("Output -> result {}", result);
		return result;
	}

	@Override
	public List<CompleteParameterBo> getCompletedFormParameters(Integer id){
		log.debug("Input parameters -> id {}", id);
		Integer formId = completedParameterizedFormRepository.findById(id).map(CompletedParameterizedForm::getParameterizedFormId).orElse(null);
		if (formId == null) return List.of();
		return completedParameterRepository.getByCompletedParameterizedFormId(id);
	}

	@Override
	public Optional<Integer> getParameterizedFormIdById(Integer id) {
		log.debug("Input parameters -> id {}", id);
		return completedParameterizedFormRepository.findById(id).map(CompletedParameterizedForm::getParameterizedFormId);
	}

	private CompletedParameter buildEntity(Integer completedFormId, NewCompletedParameterBo newCompletedParameterBo){
		return CompletedParameter.builder()
				.parameterId(newCompletedParameterBo.getParameterId())
				.completedParameterizedFormId(completedFormId)
				.parameterTextOptionId(newCompletedParameterBo.getOptionId())
				.numericValue(newCompletedParameterBo.getNumericValue())
				.textValue(newCompletedParameterBo.getTextValue())
				.snomedId(newCompletedParameterBo.getSnomedId())
				.build();
	}

}
