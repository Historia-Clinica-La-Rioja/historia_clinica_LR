package net.pladema.parameter.application.port;

import net.pladema.parameter.domain.ParameterBo;

import java.util.List;

public interface ParameterStorage {

	List<ParameterBo> findAllByIds(List<Integer> ids);

	List<ParameterBo> findByDescription(String description);
}
