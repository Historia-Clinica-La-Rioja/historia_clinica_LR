package net.pladema.parameterizedform.infrastructure.output;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.establishment.repository.InstitutionalParameterizedFormRepository;
import net.pladema.establishment.repository.entity.InstitutionalParameterizedForm;
import net.pladema.parameterizedform.application.port.output.ParameterizedFormStorage;


import net.pladema.parameterizedform.domain.enums.EFormStatus;

import net.pladema.parameterizedform.infrastructure.input.rest.dto.ParameterizedFormDto;
import net.pladema.parameterizedform.infrastructure.output.repository.ParameterizedFormRepository;

import net.pladema.parameterizedform.infrastructure.output.repository.entity.ParameterizedForm;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ParameterizedFormStorageImpl implements ParameterizedFormStorage {

	private final ParameterizedFormRepository parameterizedFormRepository;
	private final InstitutionalParameterizedFormRepository institutionalParameterizedFormRepository;

	@Override
	public void updateStatus(Integer formId) {
		log.debug("Input parameters -> formId {}", formId);
		parameterizedFormRepository.findById(formId).ifPresent(form -> {
			EFormStatus actualState = EFormStatus.map(form.getStatusId());
			EFormStatus nextState = actualState.getNextState();
			assertFormName(formId, form.getName(), nextState);
			parameterizedFormRepository.updateStatusByFormId(formId, nextState.getId());
			List<InstitutionalParameterizedForm> formEnabledInInstitutions = institutionalParameterizedFormRepository.getByParameterizedFormId(formId);
			Boolean isInactive = nextState.getId().equals(EFormStatus.INACTIVE.getId());
			if (isInactive && form.getIsDomain() && !formEnabledInInstitutions.isEmpty()) {
				institutionalParameterizedFormRepository.updateInstitutionalParameterizedFormEnabled(formId);
			}
		});
	}

	@Override
	public Page<ParameterizedFormDto> getFormByFilters(List<Short> statusIds, String name, Boolean isDomain, Pageable pageable) {
		log.debug("Input parameters -> statusIds {}, name {}, isDomain {}, pageable {}", statusIds, name, isDomain, pageable);
		List<ParameterizedForm> resultParameterizedForm = (name == null || name.trim().isEmpty()) ? parameterizedFormRepository.getFormsByStatusAndDomain(statusIds, isDomain, pageable).stream().collect(Collectors.toList()) : parameterizedFormRepository.getFormsByFilters(statusIds, isDomain, name, pageable).stream().collect(Collectors.toList());

		List<ParameterizedFormDto> result = resultParameterizedForm.stream()
				.map(this::mapEntityToDto)
				.collect(Collectors.toList());

		int minIndex = Math.min(pageable.getPageNumber() * pageable.getPageSize(), result.size());
		int maxIndex = Math.min(minIndex + pageable.getPageSize(), result.size());

		Page<ParameterizedFormDto> pageResult = new PageImpl<>(result.subList(minIndex, maxIndex), pageable, result.size());
		log.debug("Output -> result {}", pageResult);
		return pageResult;
	}

	@Override
	public Optional<Short> findFormStatus(Integer formId) {
		log.debug("Input parameters -> formId {}", formId);
		Optional<Short> result = parameterizedFormRepository.findStatusById(formId);
		log.debug("Output -> result {}", result);
		return result;
	}

	@Override
	public void updateFormEnablementInInstitution(Integer parameterizedFormId, Integer institutionId, Boolean enablement) {
		log.debug("Input parameters -> parameterizedFormId {}, institutionId {}, enablement {}", parameterizedFormId, institutionId, enablement);
		parameterizedFormRepository.findById(parameterizedFormId).ifPresent(
				parameterizedForm -> {
					if (parameterizedForm.getStatusId().equals(EFormStatus.DRAFT.getId())) {
						throw new NotFoundException("draft-form", "No se puede habilitar un formulario con estado borrador.");
					}
					institutionalParameterizedFormRepository.findByParameterizedFormIdAndInstitutionId(parameterizedFormId, institutionId).ifPresentOrElse(
							institutionalParameterizedForm -> updateInsitutionalParameterizedForm(institutionalParameterizedForm, enablement),
							() -> saveInsitutionalParameterizedForm(parameterizedForm.getId(), institutionId, enablement)
					);
				}
		);
	}

	private void assertFormName(Integer formId, String name, EFormStatus nextState) {
		Boolean nextStateIsActive = nextState.getId().equals(EFormStatus.ACTIVE.getId());
		Boolean existsFormWithName = parameterizedFormRepository.existsFormByName(formId, name);
		if (nextStateIsActive && existsFormWithName)
			throw new NotFoundException("form-with-same-name", String.format("Ya existe un formulario con ese nombre", name));
	}

	ParameterizedFormDto mapEntityToDto(ParameterizedForm entity) {
		return new ParameterizedFormDto (
				entity.getId(),
				entity.getName(),
				entity.getStatusId(),
				entity.getOutpatientEnabled(),
				entity.getInternmentEnabled(),
				entity.getEmergencyCareEnabled()
		);
	}

	private void updateInsitutionalParameterizedForm(InstitutionalParameterizedForm institutionalParameterizedForm, Boolean enablement) {
		institutionalParameterizedForm.setIsEnabled(enablement);
		institutionalParameterizedFormRepository.save(institutionalParameterizedForm);
	}

	private void saveInsitutionalParameterizedForm(Integer parameterizedFormId, Integer institutionId, Boolean enablement) {
		InstitutionalParameterizedForm institutionalParameterizedForm = new InstitutionalParameterizedForm(parameterizedFormId, institutionId, enablement);
		institutionalParameterizedFormRepository.save(institutionalParameterizedForm);
	}
}
