package net.pladema.parameter.infrastructure.output;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.parameter.application.port.ParameterStorage;
import net.pladema.parameter.domain.ParameterBo;
import net.pladema.parameter.infrastructure.output.repository.ParameterRepository;

import net.pladema.parameter.infrastructure.output.repository.entity.Parameter;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ParameterStorageImpl implements ParameterStorage {

	private final ParameterRepository parameterRepository;

	@Override
	public List<ParameterBo> findAllByIds(List<Integer> ids) {
		log.debug("Input parameters -> ids {}", ids);
		List<ParameterBo> result = parameterRepository.findAllById(ids).stream().map(this::mapToBo).collect(Collectors.toList());
		log.debug("Output -> result {}", result);
		return result;
	}

	private ParameterBo mapToBo(Parameter entity){
		return ParameterBo.builder()
				.id(entity.getId())
				.loincId(entity.getLoincId())
				.description(entity.getDescription())
				.typeId(entity.getTypeId())
				.inputCount(entity.getInputCount())
				.snomedGroupId(entity.getSnomedGroupId())
				.build();
	}

}
