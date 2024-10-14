package ar.lamansys.sgh.clinichistory.application.getcompletedformsummary;

import ar.lamansys.sgh.clinichistory.application.ports.CompletedParameterizedFormStorage;
import ar.lamansys.sgh.clinichistory.domain.completedforms.CompleteParameterBo;
import ar.lamansys.sgh.clinichistory.domain.completedforms.CompletedParameterSummaryBo;

import ar.lamansys.sgh.shared.infrastructure.input.service.forms.SharedParameterDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.forms.SharedParameterizedFormPort;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class GetCompletedFormSummary {

	private final CompletedParameterizedFormStorage completedParameterizedFormStorage;
	private final SharedParameterizedFormPort sharedParameterizedFormPort;

	public List<CompletedParameterSummaryBo> run(Integer id){
		log.debug("Input parameters -> id {}", id);
		List<CompletedParameterSummaryBo> result = new ArrayList<>();
		Optional<Integer> formId = completedParameterizedFormStorage.getParameterizedFormIdById(id);
		if (formId.isPresent()) {
			List<CompleteParameterBo> completedParameters = completedParameterizedFormStorage.getCompletedFormParameters(id);
			List<SharedParameterDto> formParameters = sharedParameterizedFormPort.getParametersByFormId(formId.get());
			result = mergeFormParameters(completedParameters, formParameters);
		}
		return result;
	}

	private List<CompletedParameterSummaryBo> mergeFormParameters(List<CompleteParameterBo> completedParameters, List<SharedParameterDto> formParameters){
		List<CompletedParameterSummaryBo> result = new ArrayList<>();

		formParameters.forEach(parameter -> {
			CompletedParameterSummaryBo completedParameterSummaryBo = new CompletedParameterSummaryBo();
			completedParameterSummaryBo.setId(parameter.getId());
			completedParameterSummaryBo.setDescription(parameter.getDescription());
			completedParameterSummaryBo.setLoincId(parameter.getLoincId());
			completedParameterSummaryBo.setType(parameter.getType());
			completedParameters.stream().filter(cp -> cp.getId().equals(parameter.getId())).findFirst()
					.ifPresent(cp -> completedParameterSummaryBo.setCompletedValue(getCompletedParameterValue(parameter, cp)));
			result.add(completedParameterSummaryBo);
		});
		return result;
	}

	private String getCompletedParameterValue(SharedParameterDto sharedParameterDto, CompleteParameterBo completeParameterBo) {
		if (completeParameterBo.isSnomed())
			return completeParameterBo.getConceptPt();
		if (completeParameterBo.getTextValue() != null)
			return completeParameterBo.getTextValue();
		if (completeParameterBo.getOptionId() != null)
			return sharedParameterDto.getTextOptions().stream().filter(option -> option.getId().equals(completeParameterBo.getOptionId())).findFirst().map(SharedParameterDto.TextOptionDto::getDescription).orElse(null);
		if (completeParameterBo.getNumericValue() != null)
			return completeParameterBo.getNumericValue().toString().concat(sharedParameterDto.getUnitOfMeasure().getCode());
		return null;
	}
}
