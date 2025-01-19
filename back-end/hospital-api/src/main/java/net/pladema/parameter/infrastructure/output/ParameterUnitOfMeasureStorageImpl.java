package net.pladema.parameter.infrastructure.output;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.parameter.application.port.ParameterUnitOfMeasureStorage;
import net.pladema.parameter.domain.ParameterUnitOfMeasureBo;
import net.pladema.parameter.infrastructure.output.repository.ParameterUnitOfMeasureRepository;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ParameterUnitOfMeasureStorageImpl implements ParameterUnitOfMeasureStorage {

	private final ParameterUnitOfMeasureRepository repository;

	@Override
	public Optional<ParameterUnitOfMeasureBo> getByParameterId(@Param("parameterId") Integer parameterId){
		log.debug("Input parameters -> parameterId {}", parameterId);
		Optional<ParameterUnitOfMeasureBo> result = repository.getByParameterId(parameterId);
		log.debug("Output -> result {}", result);
		return result;
	}

}
