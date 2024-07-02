package net.pladema.parameterizedform.infrastructure.output;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.parameterizedform.application.port.output.ParameterizedFormStorage;


import net.pladema.parameterizedform.domain.enums.EFormStatus;

import net.pladema.parameterizedform.infrastructure.output.repository.ParameterizedFormRepository;

import net.pladema.parameterizedform.infrastructure.output.repository.entity.ParameterizedForm;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ParameterizedFormStorageImpl implements ParameterizedFormStorage {

	private final ParameterizedFormRepository parameterizedFormRepository;

	@Override
	public void updateStatus(Integer formId) {
		log.debug("Input parameters -> formId {}", formId);
		parameterizedFormRepository.findById(formId).ifPresent(form -> {
			EFormStatus actualState = EFormStatus.map(form.getStatusId());
			EFormStatus nextState = actualState.getNextState();
			assertFormName(formId, form.getName(), nextState);
			parameterizedFormRepository.updateStatusByFormId(formId, nextState.getId());
		});
	}

	@Override
	public Page<ParameterizedForm> filterByStatusIdAndNameIn(List<Short> statusIds, String name, Pageable pageable) {
		return parameterizedFormRepository.getFormsByNameAndStatus(statusIds, name, pageable);
	}

	@Override
	public Optional<Short> findFormStatus(Integer formId) {
		return parameterizedFormRepository.findStatusById(formId);
	}

	private void assertFormName(Integer formId, String name, EFormStatus nextState) {
		Boolean nextStateIsActive = nextState.getId().equals(EFormStatus.ACTIVE.getId());
		Boolean existsFormWithName = parameterizedFormRepository.existsFormByName(formId, name);
		if (nextStateIsActive && existsFormWithName)
			throw new NotFoundException("form-with-same-name", String.format("Ya existe un formulario con ese nombre", name));
	}
}
