package net.pladema.parameterizedform.application.port.output;

import java.util.Optional;

import net.pladema.parameterizedform.infrastructure.output.repository.entity.ParameterizedForm;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ParameterizedFormStorage {

	void updateStatus(Integer formId);
	Page<ParameterizedForm> filterByStatusIdAndNameIn(List<Short> statusIds, String name, Pageable pageable);
	Optional<Short> findFormStatus(Integer formId);

}
