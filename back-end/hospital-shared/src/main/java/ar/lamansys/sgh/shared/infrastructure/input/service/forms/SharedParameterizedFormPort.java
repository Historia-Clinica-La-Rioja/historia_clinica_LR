package ar.lamansys.sgh.shared.infrastructure.input.service.forms;

import java.util.List;
import java.util.Optional;

public interface SharedParameterizedFormPort {

	List<SharedParameterDto> getParametersByFormId(Integer parameterizedFormId);

	Optional<String> getFormNameById(Integer id);
}
