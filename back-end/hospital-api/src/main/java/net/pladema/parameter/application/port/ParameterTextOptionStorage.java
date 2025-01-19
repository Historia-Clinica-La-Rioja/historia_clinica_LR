package net.pladema.parameter.application.port;

import net.pladema.parameter.domain.ParameterTextOptionBo;

import java.util.List;

public interface ParameterTextOptionStorage {

	List<ParameterTextOptionBo> getAllByParameterId(Integer parameterId);

}
