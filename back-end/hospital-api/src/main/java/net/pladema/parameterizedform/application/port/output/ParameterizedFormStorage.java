package net.pladema.parameterizedform.application.port.output;

import java.util.Optional;

import net.pladema.parameterizedform.infrastructure.input.rest.dto.ParameterizedFormDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ParameterizedFormStorage {

	void updateStatus(Integer formId);
	Page<ParameterizedFormDto> getFormByFilters(List<Short> statusIds, String name, Boolean isDomain, Pageable pageable);
	Optional<Short> findFormStatus(Integer formId);
	void updateFormEnablementInInstitution(Integer parameterizedFormId, Integer institutionId, Boolean enablement);

}
