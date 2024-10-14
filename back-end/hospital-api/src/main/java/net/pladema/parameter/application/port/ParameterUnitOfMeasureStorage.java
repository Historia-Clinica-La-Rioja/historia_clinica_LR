package net.pladema.parameter.application.port;

import net.pladema.parameter.domain.ParameterUnitOfMeasureBo;

import java.util.Optional;

public interface ParameterUnitOfMeasureStorage {

	Optional<ParameterUnitOfMeasureBo> getByParameterId(Integer parameterId);

}
