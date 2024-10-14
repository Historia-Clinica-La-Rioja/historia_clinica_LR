package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.generateFile;

import ar.lamansys.sgh.clinichistory.application.getcompletedformsummary.GetCompletedFormSummary;
import ar.lamansys.sgh.clinichistory.domain.completedforms.CompleteParameterBo;
import ar.lamansys.sgh.clinichistory.domain.completedforms.CompleteParameterizedFormBo;
import ar.lamansys.sgh.shared.domain.forms.enums.EParameterType;
import ar.lamansys.sgh.shared.infrastructure.input.service.forms.SharedParameterDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.forms.SharedParameterizedFormPort;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MapCompletedForms {

	private final SharedParameterizedFormPort parameterizedFormPort;

	public MapCompletedForms(SharedParameterizedFormPort parameterizedFormPort){
		this.parameterizedFormPort = parameterizedFormPort;
	}

	public List<CompletedFormDto> execute(List<CompleteParameterizedFormBo> completedForms){
		if (completedForms == null || completedForms.isEmpty()) return null;
		return completedForms.stream()
				.map(cp -> {
					String formName = parameterizedFormPort.getFormNameById(cp.getId()).orElse("");
					List<CompletedFormDto.Parameter> parameters = mapCompletedParameters(cp.getId(), cp.getParameters());
					return new CompletedFormDto(cp.getId(), formName, parameters);
				}).collect(Collectors.toList());
	}

	private List<CompletedFormDto.Parameter> mapCompletedParameters(Integer formId, List<CompleteParameterBo> completedParameters){
		List<SharedParameterDto> allFormParameters = parameterizedFormPort.getParametersByFormId(formId);
		Map<Short, List<String>> parametersMap = allFormParameters.stream()
				.collect(Collectors.toMap(
						SharedParameterDto::getOrderNumber,
						p -> {
							var parameterDto = completedParameters.stream()
									.filter(cp -> cp.getId().equals(p.getId()))
									.findFirst()
									.orElse(null);
							String completedParameterValue = (parameterDto != null) ? getCompletedParameterValue(parameterDto, p) : null;
							return completedParameterValue != null ? new ArrayList<>(List.of(completedParameterValue)) : new ArrayList<>();
						},
						(existing, replacement) -> {  // Handle case where the same orderNumber exists multiple times
							existing.addAll(replacement);
							return existing;
						}
				));

		return parametersMap.entrySet().stream()
				.map(entry -> {
					Short orderNumber = entry.getKey();
					List<String> values = entry.getValue();
					String parameterDescription = allFormParameters.stream()
							.filter(p -> p.getOrderNumber().equals(orderNumber))
							.map(SharedParameterDto::getDescription)
							.findFirst().get();
					String parameterValue = values.isEmpty() ? null : String.join(" | ", values);
					return new CompletedFormDto.Parameter(parameterDescription, parameterValue);
				})
				.collect(Collectors.toList());
	}

	private String getCompletedParameterValue(CompleteParameterBo completeParameterBo, SharedParameterDto sharedParameterDto){
		if (sharedParameterDto.getType().equals(EParameterType.NUMERIC))
			return completeParameterBo.getNumericValue().toString().concat(" ").concat(sharedParameterDto.getUnitOfMeasure().getCode());
		if (sharedParameterDto.getType().equals(EParameterType.OPTIONS_LIST))
			return sharedParameterDto.getTextOptions().stream()
					.filter(p -> p.getId().equals(completeParameterBo.getOptionId()))
					.map(SharedParameterDto.TextOptionDto::getDescription)
					.findFirst()
					.orElse(null);
		if (sharedParameterDto.getType().equals(EParameterType.FREE_TEXT))
			return completeParameterBo.getTextValue();
		return completeParameterBo.getConceptPt();
	}

}

