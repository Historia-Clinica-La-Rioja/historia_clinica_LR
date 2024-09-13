package net.pladema.parameter.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.loinc.application.port.LoincCodeStoragePort;
import net.pladema.parameter.application.port.ParameterStorage;
import net.pladema.parameter.application.port.ParameterTextOptionStorage;
import net.pladema.parameter.application.port.ParameterUnitOfMeasureStorage;
import net.pladema.parameter.domain.ParameterBo;
import net.pladema.parameter.domain.ParameterCompleteDataBo;
import ar.lamansys.sgh.shared.domain.forms.enums.EParameterType;
import net.pladema.parameterizedform.application.port.output.ParameterizedFormParameterStorage;

import net.pladema.snowstorm.repository.SnomedGroupRepository;

import net.pladema.snowstorm.repository.entity.SnomedGroup;

import net.pladema.snowstorm.services.domain.semantics.SnomedECL;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetParameterizedFormParametersByFormId {

	private final ParameterStorage parameterStorage;
	private final ParameterizedFormParameterStorage parameterizedFormParameterStorage;
	private final ParameterTextOptionStorage parameterTextOptionStorage;
	private final SnomedGroupRepository snomedGroupRepository;
	private final ParameterUnitOfMeasureStorage parameterUnitOfMeasureStorage;
	private final LoincCodeStoragePort loincCodeStoragePort;

	public List<ParameterCompleteDataBo> run (Integer formId){
		log.debug("Input parameters -> formId {}", formId);

		List<Integer> parameterIds = parameterizedFormParameterStorage.findParameterIdsByFormId(formId);

		List<ParameterCompleteDataBo> result = parameterStorage.findAllByIds(parameterIds)
				.stream()
				.map(parameter -> completeParameterData(parameter, formId))
				.sorted(Comparator.comparing(ParameterCompleteDataBo::getOrderNumber, Comparator.nullsLast(Comparator.naturalOrder()))
						.thenComparing(ParameterCompleteDataBo::getId))
				.collect(Collectors.toList());
		
		log.debug("Output -> {}", result);
		return result;
	}

	private ParameterCompleteDataBo completeParameterData (ParameterBo parameter, Integer formId){
		ParameterCompleteDataBo result = new ParameterCompleteDataBo();
		result.setId(parameter.getId());
		result.setType(EParameterType.map(parameter.getTypeId()));
		result.setDescription(parameter.getDescription());
		result.setInputCount(parameter.getInputCount());
		result.setLoincId(parameter.getLoincId());
		if (parameter.getSnomedGroupId() != null) {
			String ecl = snomedGroupRepository.findById(parameter.getSnomedGroupId()).map(SnomedGroup::getDescription).orElse("");
			result.setEcl(SnomedECL.map(ecl));
		}
		if (parameter.getLoincId() != null){
			result.setDescription(loincCodeStoragePort.getDescriptionById(parameter.getLoincId()).orElse(null));
		}
		if (parameter.getTypeId().equals(EParameterType.OPTIONS_LIST.getId()))
			result.setTextOptions(parameterTextOptionStorage.getAllByParameterId(parameter.getId()));
		if (parameter.getTypeId().equals(EParameterType.NUMERIC.getId()))
			result.setUnitOfMeasure(parameterUnitOfMeasureStorage.getByParameterId(parameter.getId()).orElse(null));
		result.setOrderNumber(parameterizedFormParameterStorage.getOrderNumberByFormIdAndParameterId(formId, parameter.getId()).orElse(null));
		return result;
	}

}
