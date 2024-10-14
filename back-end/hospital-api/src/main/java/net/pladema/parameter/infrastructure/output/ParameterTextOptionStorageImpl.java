package net.pladema.parameter.infrastructure.output;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.parameter.application.port.ParameterTextOptionStorage;
import net.pladema.parameter.domain.ParameterTextOptionBo;

import net.pladema.parameter.infrastructure.output.repository.ParameterTextOptionRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ParameterTextOptionStorageImpl implements ParameterTextOptionStorage {

	private final ParameterTextOptionRepository repository;

	@Override
	public List<ParameterTextOptionBo> getAllByParameterId(Integer parameterId) {
		log.debug("Input parameters -> parameterId {}", parameterId);
		List<ParameterTextOptionBo> result = repository.getAllByParameterId(parameterId);
		log.debug("Output -> result {}", result);
		return result;
	}

}
