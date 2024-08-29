package ar.lamansys.sgh.shared.infrastructure.input.service.forms;

import java.util.List;

public interface SharedParameterizedFormPort {

	List<SharedParameterDto> getParametersByFormId(Integer parameterizedFormId);

}
